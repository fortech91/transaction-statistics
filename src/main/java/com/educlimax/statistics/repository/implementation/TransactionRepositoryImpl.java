package com.educlimax.statistics.repository.implementation;

import com.educlimax.statistics.model.Transaction;
import com.educlimax.statistics.repository.TransactionRepository;
import com.educlimax.statistics.util.IndexGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * An memory-based transaction repository.
 * This repository is implemented with an atomic data structure for thread-safety.
 * To free memory, old transactions are nullified and removed when transactions are fetched
 * and when the transactions are being copied from one transaction store to another
 * as the transactions grow bigger.
 *
 *
 * @author FortunatusE
 * @date 12/7/2018
 */

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Logger logger = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    private static final int CUT_OFF_SECONDS = 60;
    private static final int INITIAL_ARRAY_CAPACITY = 1000;

    private final Clock clock;
    private AtomicReferenceArray<Transaction> transactionStore;


    @Autowired
    public TransactionRepositoryImpl(Clock clock) {
        this.clock = clock;
        this.transactionStore = new AtomicReferenceArray<>(INITIAL_ARRAY_CAPACITY);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Transaction save(Transaction transaction) {

        logger.debug("Saving transaction: {}", transaction);

        int index = getNextIndex();
        logger.debug("Next Id: {}", index);
        transaction.setId(index);
        transactionStore.set(index, transaction);
        return transaction;
    }

    /**
     * Returns the next transaction index to be used in storing the transaction in the store.
     * It uses an IndexGenerator to get the next index.
     * This method ensures that the index returned is always in sync with the transaction store..
     *
     * @return the next index for transaction
     */
    private int getNextIndex() {

        int nextIndex = IndexGenerator.nextIndex();

        if (nextIndex == transactionStore.length()) {
            logger.debug("Index [{}] reached length of transaction store", nextIndex);
            //increase the capacity of the data store
            AtomicReferenceArray<Transaction> newTransactionStore = createNewTransactionStore(transactionStore);
            transactionStore = newTransactionStore;
            logger.debug("New transaction store capacity: {}", transactionStore.length());
            nextIndex = IndexGenerator.nextIndex();
        }
        return nextIndex;
    }

    /**
     * Creates and returns a new transaction store.
     * The transaction is created when the existing store has reached it's capacity.
     * Existing transactions are copied to the store while discarding the old transactions.
     * @param existingStore the existing store that will be replaced.
     * @return the new transaction store
     */
    private AtomicReferenceArray<Transaction> createNewTransactionStore(AtomicReferenceArray<Transaction> existingStore) {

        logger.debug("Creating new transaction store as existing one is already filled up");

        int arrayCapacity = existingStore.length() * 2;
        AtomicReferenceArray<Transaction> newTransactionStore = new AtomicReferenceArray<>(arrayCapacity);

        int numOfNullTransactions = 0;
        int newIndex = 0;
        //copy transactions to new store
        for (int index = 0; index < existingStore.length(); index++) {
            Transaction transaction = existingStore.get(index);
            if (transaction != null) {
                newTransactionStore.set(newIndex++, transaction);
            } else {
                ++numOfNullTransactions;
            }
        }
        int numOfCopiedTransactions = transactionStore.length() - numOfNullTransactions;
        logger.debug("Copied {} transactions to new store", numOfCopiedTransactions);
        IndexGenerator.resetCounter(numOfCopiedTransactions);
        return newTransactionStore;
    }


    /**
     * {@inheritDoc}
     * @return
     */
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


/**
 * Checks if the given transaction is old.
 * @param transaction the transaction
 * @return true if the transaction is old
 * */
    private boolean isOldTransaction(Transaction transaction) {

        Instant transactionTime = transaction.getTimeStamp();
        Instant now = clock.instant();
        long duration = Duration.between(transactionTime, now).getSeconds();
        return duration >= CUT_OFF_SECONDS;
    }


    /**
     * {@inheritDoc}
     */
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
