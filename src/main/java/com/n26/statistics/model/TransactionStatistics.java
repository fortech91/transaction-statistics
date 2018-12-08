package com.n26.statistics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionStatistics {

	@JsonProperty("avg")
	private String avg;

	@JsonProperty("min")
	private String min;

	@JsonProperty("max")
	private String max;

	@JsonProperty("count")
	private int count;

	@JsonProperty("sum")
	private String sum;

}