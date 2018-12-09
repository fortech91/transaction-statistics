package com.n26.statistics.repository;

import com.n26.statistics.model.Transaction;

import java.util.List;


/**
 * A transaction repository.
 *
 * @author FortunatusE
 * @date 12/7/2018
 */
public interface TransactionRepository {

    /**
     * Saves the give transaction
     * @param transaction the transaction
     * @return the saved transaction
     */
    Transaction save(Transaction transaction);

    /**
     * Returns non-null transactions
     * @return transactions
     */
    List<Transaction> getTransactions();


    /**
     * Deletes all transactions
     */
    void deleteAllTransactions();
}
