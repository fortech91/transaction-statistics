package com.educlimax.statistics.model;


import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author FortunatusE
 * @date 12/7/2018
 */

public class Transaction {

    private Integer id;
    private BigDecimal amount;
    private Instant timeStamp;

    public Transaction(BigDecimal amount, Instant timeStamp) {
        this.amount = amount;
        this.timeStamp = timeStamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
