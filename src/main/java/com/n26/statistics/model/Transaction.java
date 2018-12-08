package com.n26.statistics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Data
public class Transaction {

    private BigDecimal amount;
    private Instant timeStamp;

    public Transaction(BigDecimal amount, Instant timeStamp) {
        this.amount = amount;
        this.timeStamp = timeStamp;
    }
}
