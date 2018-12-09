package com.n26.statistics.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Data
public class Transaction {

    private Integer id;
    private BigDecimal amount;
    private Instant timeStamp;

    public Transaction(BigDecimal amount, Instant timeStamp) {
        this.amount = amount;
        this.timeStamp = timeStamp;
    }
}
