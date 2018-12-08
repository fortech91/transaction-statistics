package com.n26.statistics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Data
public class TransactionStatisticsDTO {

	@JsonProperty("sum")
	private String sum;

	@JsonProperty("avg")
	private String avg;

	@JsonProperty("max")
	private String max;

	@JsonProperty("min")
	private String min;

	@JsonProperty("count")
	private long count;


}