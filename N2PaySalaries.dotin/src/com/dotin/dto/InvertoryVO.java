package com.dotin.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class InvertoryVO implements Serializable {

    String depositNumber;
    BigDecimal amount;

    public InvertoryVO(String depositNumber, BigDecimal amount) {
        this.depositNumber = depositNumber;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return depositNumber + "\t" + amount + "\n";
    }

    public String getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(String depositNumber) {
        this.depositNumber = depositNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}