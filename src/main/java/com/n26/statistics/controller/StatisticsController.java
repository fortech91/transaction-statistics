package com.n26.statistics.controller;

import com.n26.statistics.dto.TransactionDTO;
import com.n26.statistics.model.TransactionStatistics;
import com.n26.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author FortunatusE
 * @date 12/8/2018
 */


@RestController
public class StatisticsController {


    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostMapping("/transactions")
    public ResponseEntity addTransaction(@RequestBody TransactionDTO transaction){

        HttpStatus status = statisticsService.addTransaction(transaction);
        return ResponseEntity.status(status).build();
    }


    @GetMapping("/statistics")
    public ResponseEntity getTransactionStatistics(){

        TransactionStatistics statistics = statisticsService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    @DeleteMapping("/transactions")
    public ResponseEntity deleteTransactions(){

        HttpStatus status = statisticsService.deleteTransactions();
        return ResponseEntity.status(status).build();
    }
}
