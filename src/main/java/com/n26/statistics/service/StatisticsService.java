package com.n26.statistics.service;

import com.n26.statistics.dto.TransactionDTO;
import com.n26.statistics.dto.TransactionStatisticsDTO;
import org.springframework.http.HttpStatus;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */
public interface StatisticsService {


    HttpStatus addTransaction(TransactionDTO transaction);

    TransactionStatisticsDTO getStatistics();

    HttpStatus deleteTransactions();
}
