package com.dotin.dto;

import java.io.Serializable;
import java.math.BigDecimal;


public class TransactionVO implements Serializable {
    String debtorDN;
    String creditorDN;
    BigDecimal amount;

    public TransactionVO(String debtorDN, String creditorDN, BigDecimal amount) {

        this.debtorDN = debtorDN;
        this.creditorDN = creditorDN;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return debtorDN + "\t" + creditorDN + "\t" + amount + "\n";
    }

    public String getDebtorDN() {
        return debtorDN;
    }

    public void setDebtorDN(String debtorDN) {
        this.debtorDN = debtorDN;
    }

    public String getCreditorDN() {
        return creditorDN;
    }

    public void setCreditorDN(String creditorDN) {
        this.creditorDN = creditorDN;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
