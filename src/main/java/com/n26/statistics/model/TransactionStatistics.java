package com.n26.statistics.model;

import java.math.BigDecimal;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */


public class TransactionStatistics {

	private BigDecimal sum;
	private BigDecimal avg;
	private BigDecimal max;
	private BigDecimal min;
	private long count;


	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getAvg() {
		return avg;
	}

	public void setAvg(BigDecimal avg) {
		this.avg = avg.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public TransactionStatistics initialValue(){

		setSum(new BigDecimal("0.00"));
		setAvg(new BigDecimal("0.00"));
		setMax(new BigDecimal("0.00"));
		setMin(new BigDecimal("0.00"));
		setCount(0L);
		return this;
	}

	@Override
	public String toString() {
		return "TransactionStatistics{" +
				"sum=" + sum +
				", avg=" + avg +
				", max=" + max +
				", min=" + min +
				", count=" + count +
				'}';
	}
}