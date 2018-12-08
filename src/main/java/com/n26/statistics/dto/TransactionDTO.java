package com.n26.statistics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("timestamp")
    private String timeStamp;
}
