package com.n26.statistics.repository;

import com.n26.statistics.model.Transaction;

import java.util.List;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */
public interface TransactionRepository {

    void save(Transaction transaction);

    void discardOldTransactions();

}
