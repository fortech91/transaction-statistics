package com.n26.statistics.service;

import com.n26.statistics.dto.TransactionDTO;
import com.n26.statistics.model.Transaction;
import com.n26.statistics.model.TransactionStatistics;
import org.springframework.http.HttpStatus;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */
public interface TransactionService {


    HttpStatus addTransaction(TransactionDTO transaction);

    TransactionStatistics getStatistics();
}
