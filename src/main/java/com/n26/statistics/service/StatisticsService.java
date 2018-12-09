package com.n26.statistics.service;

import com.n26.statistics.dto.Response;
import com.n26.statistics.dto.TransactionDTO;

/**
 *  Processes transaction data and computes the statistics
 *
 * @author FortunatusE
 * @date 12/7/2018
 */
public interface StatisticsService {


    /**
     * Adds the given transaction details
     * @param transaction the transaction to be added
     * @return response with status of the operation
     */
    Response addTransaction(TransactionDTO transaction);


    /**
     * Returns the computed statistics of the transactions
     * @return response containing the statistics
     */
    Response getStatistics();

    /**
     * Deletes all stored transactions
     * @return response with status of the operation
     */
    Response deleteTransactions();
}
