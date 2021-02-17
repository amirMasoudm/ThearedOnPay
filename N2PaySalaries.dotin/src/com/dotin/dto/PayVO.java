package com.dotin.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayVO implements Serializable {
    OprationType oprationType;
    String depositNumber;
    BigDecimal amount;


    public PayVO(OprationType oprationType, String depositNumber, BigDecimal amount) {
        this.amount = amount;
        this.depositNumber = depositNumber;
        this.oprationType = oprationType;
    }

    public OprationType getOprationType() {
        return oprationType;
    }

    @Override
    public String toString() {
        return oprationType + "\t" + depositNumber + "\t" + amount + "\n";
    }

    public void setOprationType(OprationType oprationType) {
        this.oprationType = oprationType;
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
