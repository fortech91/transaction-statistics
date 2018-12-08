package com.n26.statistics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Data
@Builder
public class TransactionStatistics {

	@JsonProperty("sum")
	private BigDecimal sum;

	@JsonProperty("avg")
	private BigDecimal avg;

	@JsonProperty("max")
	private BigDecimal max;

	@JsonProperty("min")
	private BigDecimal min;

	@JsonProperty("count")
	private long count;

}