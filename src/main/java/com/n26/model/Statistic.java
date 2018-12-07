package com.n26.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

@Data
public class Statistic {

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