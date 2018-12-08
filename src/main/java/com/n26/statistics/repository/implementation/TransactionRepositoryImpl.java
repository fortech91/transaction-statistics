package com.n26.statistics.repository.implementation;

import com.n26.statistics.model.Transaction;
import com.n26.statistics.repository.TransactionRepository;
import com.n26.statistics.util.IndexCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Logger logger = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    private static final int CUT_OFF_SECONDS = 60;
    private static final int INITIAL_ARRAY_CAPACITY = 62;

    private final AtomicReferenceArray<Transaction> transactionRepository;


    public TransactionRepositoryImpl() {
        this.transactionRepository = new AtomicReferenceArray<>(INITIAL_ARRAY_CAPACITY);
    }

    @Override
    public void save(Transaction transaction) {

        logger.debug("Saving transaction: {}", transaction);

        int index = IndexCounter.nextIndex();
        logger.debug("Next index: {}", index);
        transactionRepository.set(index, transaction);
    }

    @Override
    public void discardOldTransactions(){

        for(int i = 0; i < transactionRepository.length(); i++) {
            Transaction transaction = transactionRepository.get(i);
            if(isOldTransaction(transaction)){
                transactionRepository.compareAndSet(i, transaction, null);
            }
        }
    }

    private boolean isOldTransaction(Transaction transaction){

        Instant transactionTime = transaction.getTimeStamp();
        Instant now = Instant.now();
        long duration = Duration.between(transactionTime, now).getSeconds();
        return duration > CUT_OFF_SECONDS;
    }

}
