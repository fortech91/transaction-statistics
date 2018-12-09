package com.n26.statistics.repository.implementation;

import com.n26.statistics.model.Transaction;
import com.n26.statistics.repository.TransactionRepository;
import com.n26.statistics.util.IndexCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Logger logger = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    private static final int CUT_OFF_SECONDS = 60;
    private static int INITIAL_ARRAY_CAPACITY = 5000;

    private AtomicReferenceArray<Transaction> transactionStore;


    public TransactionRepositoryImpl() {
        this.transactionStore = new AtomicReferenceArray<>(INITIAL_ARRAY_CAPACITY);
    }

    @Override
    public Transaction save(Transaction transaction) {

        logger.debug("Saving transaction: {}", transaction);

        int index = getIndex();
        logger.debug("Next index: {}", index);
        transactionStore.set(index, transaction);
        transaction.setId(index);
        return transaction;
    }

    private int getIndex() {

        for (int index = 0; index < transactionStore.length(); index++) {
            Transaction transaction = transactionStore.get(index);
            if (transaction == null) {
                return index;
            }
        }
        int nextIndex = IndexCounter.nextIndex();

        if (nextIndex == transactionStore.length()) {
            //increase the capacity of the data store
            AtomicReferenceArray<Transaction> newTransactionStore = createNewTransactionStore(transactionStore);
            transactionStore = newTransactionStore;
        }
        return nextIndex;
    }

    private AtomicReferenceArray<Transaction> createNewTransactionStore(AtomicReferenceArray<Transaction> existingStore) {

        logger.debug("Creating new transaction store as existing one is already filled up");

        int arrayCapacity = existingStore.length() * 2;

        AtomicReferenceArray<Transaction> newTransactionStore = new AtomicReferenceArray<Transaction>(arrayCapacity);

        for (int index = 0; index < existingStore.length(); index++) {
            Transaction transaction = existingStore.get(index);
            newTransactionStore.set(index, transaction);
        }
        return newTransactionStore;
    }


    @Override
    public List<Transaction> getTransactions() {

        logger.debug("Getting transactions");

        ArrayList<Transaction> transactions = new ArrayList<>();

        for (int index = 0; index < transactionStore.length(); index++) {
            Transaction transaction = transactionStore.get(index);
            if (transaction != null) {
                if (isOldTransaction(transaction)) {
                    //discard the transaction by nullifying the reference
                    transactionStore.compareAndSet(index, transaction, null);
                } else {
                    transactions.add(transaction);
                }
            }
        }
        return transactions;
    }

    private boolean isOldTransaction(Transaction transaction) {

        Instant transactionTime = transaction.getTimeStamp();
        Instant now = Instant.now();
        long duration = Duration.between(transactionTime, now).getSeconds();
        return duration >= CUT_OFF_SECONDS;
    }


    @Override
    public void deleteAllTransactions() {

        logger.debug("Deleting all transactions");
        for (int index = 0; index < transactionStore.length(); index++) {
            //nullify all transaction references
            transactionStore.set(index, null);
        }
        logger.warn("Deleted all transactions");
    }
}
