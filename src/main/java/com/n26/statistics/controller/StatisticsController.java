package com.n26.statistics.controller;

import com.n26.statistics.dto.Response;
import com.n26.statistics.dto.TransactionDTO;
import com.n26.statistics.dto.TransactionStatisticsDTO;
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

        Response response = statisticsService.addTransaction(transaction);
        System.out.println("Response: "+response);
        return ResponseEntity.status(response.getStatus()).build();
    }


    @GetMapping("/statistics")
    public ResponseEntity getTransactionStatistics(){

        Response response = statisticsService.getStatistics();
        return ResponseEntity.status(response.getStatus()).body(response.getBody());
    }

    @DeleteMapping("/transactions")
    public ResponseEntity deleteTransactions(){

        Response response = statisticsService.deleteTransactions();
        return ResponseEntity.status(response.getStatus()).build();
    }
}
