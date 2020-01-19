package com.educlimax.statistics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

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




	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getAvg() {
		return avg;
	}

	public void setAvg(String avg) {
		this.avg = avg;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "TransactionStatisticsDTO{" +
				"sum='" + sum + '\'' +
				", avg='" + avg + '\'' +
				", max='" + max + '\'' +
				", min='" + min + '\'' +
				", count=" + count +
				'}';
	}
}
