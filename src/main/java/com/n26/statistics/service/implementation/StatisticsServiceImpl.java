package com.n26.statistics.service.implementation;

import com.n26.statistics.dto.TransactionDTO;
import com.n26.statistics.dto.TransactionStatisticsDTO;
import com.n26.statistics.exception.TransactionException;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.model.TransactionStatistics;
import com.n26.statistics.repository.TransactionRepository;
import com.n26.statistics.service.StatisticsService;
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

    @Override
    public HttpStatus addTransaction(TransactionDTO transactionDTO) {

        logger.debug("Adding transaction: {}", transactionDTO);

        try {
            Transaction transaction = convertTransactionDtoToModel(transactionDTO);
            if (isOlderThan60Seconds(transaction)) {
                logger.debug("Transaction is older than 60 seconds and will be discarded");
                return HttpStatus.NO_CONTENT;
            }
            logger.debug("Transaction is still young and will be added");
            Transaction savedTransaction = transactionRepository.save(transaction);
            logger.info("Added transaction: {}", savedTransaction);
            return HttpStatus.CREATED;

        } catch (TransactionException exception) {
            logger.error(exception.getMessage());
            return exception.getStatus();
        }
    }

    /**
     * Converts the transaction DTO to a domain model
     *
     * @param transactionDTO the dto to be converted
     * @return the domain model
     * @throws TransactionException if an error occurs during the conversion
     */
    private Transaction convertTransactionDtoToModel(TransactionDTO transactionDTO) throws TransactionException {

        final BigDecimal amount;
        final Instant timeStamp;

        try {
            amount = new BigDecimal(transactionDTO.getAmount());
            timeStamp = Instant.parse(transactionDTO.getTimeStamp());
        } catch (NumberFormatException | DateTimeParseException e) {
            logger.error("Could not parse data: {}", e.getMessage());
            throw new TransactionException("Could not parse transaction data", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (isFutureTime(timeStamp)) {
            throw new TransactionException("Transaction date [" + timeStamp + "] is in the future and discarded", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        logger.debug("Amount: {}", amount);
        logger.debug("Timestamp: {}", timeStamp);

        Transaction transaction = new Transaction(amount, timeStamp);
        return transaction;
    }

    private boolean isFutureTime(Instant instant) {

        Instant currentTime = clock.instant();
        logger.debug("Current time: {}", currentTime);
        return instant.isAfter(currentTime);

    }


    private boolean isOlderThan60Seconds(Transaction transaction) {

        logger.debug("Checking if transaction is older than last 60 secs");
        Instant timeStamp = transaction.getTimeStamp();
        Instant now = clock.instant();
        long duration = Duration.between(timeStamp, now).getSeconds();
        return duration >= ONE_MINUTE;
    }


    @Override
    public TransactionStatisticsDTO getStatistics() {

        logger.debug("Retrieving transaction statistics");

        List<Transaction> transactions = transactionRepository.getTransactions();
        logger.debug("Transactions length: {}", transactions.size());

        if (transactions.isEmpty()) {
            return convertTransactionStatisticsModelToDto(new TransactionStatistics().initialValue());
        }
        DoubleSummaryStatistics statistics = transactions.stream().collect(Collectors.summarizingDouble(transaction -> transaction.getAmount().doubleValue()));

        TransactionStatistics transactionStatistics = new TransactionStatistics();
        transactionStatistics.setSum(new BigDecimal(statistics.getSum()));
        transactionStatistics.setAvg(new BigDecimal(statistics.getAverage()));
        transactionStatistics.setMax(new BigDecimal(statistics.getMax()));
        transactionStatistics.setMin(new BigDecimal(statistics.getMin()));
        transactionStatistics.setCount(statistics.getCount());
        logger.debug("Statistics: {}", statistics);
        return convertTransactionStatisticsModelToDto(transactionStatistics);
    }

    @Override
    public HttpStatus deleteTransactions() {

        logger.debug("Deleting all transactions");
        transactionRepository.deleteAllTransactions();
        return HttpStatus.NO_CONTENT;
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
}
