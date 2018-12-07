package com.n26.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Data
public class Transaction {

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("timestamp")
    private LocalDateTime timeStamp;
}
