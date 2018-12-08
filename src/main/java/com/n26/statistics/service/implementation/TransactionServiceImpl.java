package com.n26.statistics.service.implementation;

import com.n26.statistics.dto.TransactionDTO;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.model.TransactionStatistics;
import com.n26.statistics.repository.TransactionRepository;
import com.n26.statistics.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */


@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public HttpStatus addTransaction(TransactionDTO transaction) {

        return null ;
    }

    @Override
    public TransactionStatistics getStatistics() {
        return null;
    }

    private boolean isOlderThan60Seconds(Instant transactionTime){

        Instant now = Instant.now();
        long duration = Duration.between(transactionTime, now).getSeconds();
        return duration > 60;
    }

}
