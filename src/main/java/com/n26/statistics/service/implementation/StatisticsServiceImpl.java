package com.n26.statistics.service.implementation;

import com.n26.statistics.dto.Response;
import com.n26.statistics.dto.TransactionDTO;
import com.n26.statistics.dto.TransactionStatisticsDTO;
import com.n26.statistics.exception.TransactionException;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.model.TransactionStatistics;
import com.n26.statistics.repository.TransactionRepository;
import com.n26.statistics.service.StatisticsService;
import com.n26.statistics.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Processes transaction data and computes the statistics.
 * This service uses the UTC for time references as provided by the
 * system clock. The statistics computed by this service are for transactions
 * that occurred in the last 60 seconds.
 *
 * @author FortunatusE
 * @date 12/7/2018
 */


@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final int ONE_MINUTE = 60;
    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final Clock clock;


    @Autowired
    public StatisticsServiceImpl(TransactionRepository transactionRepository, Clock clock) {
        this.transactionRepository = transactionRepository;
        this.clock = clock;
    }


    /**
     *{@inheritDoc}
     */
    @Override
    public Response addTransaction(TransactionDTO transactionDTO) {

        logger.debug("Adding transaction: {}", transactionDTO);

        try {
            Transaction transaction = validateAndConvertTransactionDtoToModel(transactionDTO);
            if (isOldTransaction(transaction)) {
                logger.debug("Transaction is older than 60 seconds and will be discarded");
                return ResponseUtils.createResponse(HttpStatus.NO_CONTENT);
            }
            logger.debug("Transaction is still young and will be added");
            Transaction savedTransaction = transactionRepository.save(transaction);
            logger.info("Added transaction: {}", savedTransaction);
            return ResponseUtils.createResponse(HttpStatus.CREATED);

        } catch (TransactionException exception) {
            logger.error(exception.getMessage());
            return ResponseUtils.createResponse(exception.getStatus());
        }
    }

    /**
     * Validates the transaction details and converts the DTO to a domain model.
     * @param transactionDTO the transaction to be validated
     * @return the domain model
     * @throws TransactionException if any of the transaction data cannot be parsed or the time is in the future
     */
    private Transaction validateAndConvertTransactionDtoToModel(TransactionDTO transactionDTO) throws TransactionException {

        final BigDecimal amount;
        final Instant timeStamp;

        try {
            amount = new BigDecimal(transactionDTO.getAmount());
            timeStamp = Instant.parse(transactionDTO.getTimeStamp());
        } catch (NumberFormatException | DateTimeParseException e) {
            logger.error("Could not parse data: {}", e.getMessage());
            throw new TransactionException("Could not parse transaction data", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (isFutureDate(timeStamp)) {
            throw new TransactionException("Transaction date [" + timeStamp + "] is in the future and discarded", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        logger.debug("Amount: {}", amount);
        logger.debug("Timestamp: {}", timeStamp);

        Transaction transaction = new Transaction(amount, timeStamp);
        return transaction;
    }

    /**
     * Checks if the given date instant is in the future
     * @param instant the date instant
     * @return true if the date instant is in the future
     */
    private boolean isFutureDate(Instant instant) {

        Instant currentTime = clock.instant();
        logger.debug("Current time: {}", currentTime);
        return instant.isAfter(currentTime);

    }


    /**
     * Checks if the given transaction is old.
     * A transaction is considered old if the time stamp is not
     * within the last 60 seconds.
     * @param transaction the transaction
     * @return true if the transaction is old
     */
    private boolean isOldTransaction(Transaction transaction) {

        logger.debug("Checking if transaction is older than last 60 secs");
        Instant timeStamp = transaction.getTimeStamp();
        Instant now = clock.instant();
        long duration = Duration.between(timeStamp, now).getSeconds();
        return duration >= ONE_MINUTE;
    }


    /**
     *{@inheritDoc}
     */
    @Override
    public Response getStatistics() {

        logger.debug("Retrieving transaction statistics");

        List<Transaction> transactions = transactionRepository.getTransactions();
        logger.debug("Transactions length: {}", transactions.size());

        if (transactions.isEmpty()) {
            TransactionStatisticsDTO transactionStatisticsDTO = convertTransactionStatisticsModelToDto(new TransactionStatistics().initialValue());
            ResponseUtils.createResponse(HttpStatus.OK, transactionStatisticsDTO);
        }
        DoubleSummaryStatistics statistics = transactions.stream().collect(Collectors.summarizingDouble(transaction -> transaction.getAmount().doubleValue()));

        TransactionStatistics transactionStatistics = new TransactionStatistics();
        transactionStatistics.setSum(new BigDecimal(statistics.getSum()));
        transactionStatistics.setAvg(new BigDecimal(statistics.getAverage()));
        transactionStatistics.setMax(new BigDecimal(statistics.getMax()));
        transactionStatistics.setMin(new BigDecimal(statistics.getMin()));
        transactionStatistics.setCount(statistics.getCount());
        TransactionStatisticsDTO transactionStatisticsDTO = convertTransactionStatisticsModelToDto(transactionStatistics);
        logger.debug("Statistics: {}", transactionStatisticsDTO);
        return ResponseUtils.createResponse(HttpStatus.OK, transactionStatisticsDTO);


    }

    private TransactionStatisticsDTO convertTransactionStatisticsModelToDto(TransactionStatistics transactionStatistics) {

        TransactionStatisticsDTO transactionStatisticsDTO = new TransactionStatisticsDTO();
        transactionStatisticsDTO.setSum(transactionStatistics.getSum().toPlainString());
        transactionStatisticsDTO.setAvg(transactionStatistics.getAvg().toPlainString());
        transactionStatisticsDTO.setMax(transactionStatistics.getMax().toPlainString());
        transactionStatisticsDTO.setMin(transactionStatistics.getMin().toPlainString());
        transactionStatisticsDTO.setCount(transactionStatistics.getCount());
        return transactionStatisticsDTO;
    }


    /**
     *{@inheritDoc}
     */
    @Override
    public Response deleteTransactions() {

        logger.debug("Deleting all transactions");
        transactionRepository.deleteAllTransactions();
        return ResponseUtils.createResponse(HttpStatus.NO_CONTENT);
    }


}
