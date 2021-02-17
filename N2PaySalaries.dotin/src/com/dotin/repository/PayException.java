package com.dotin.repository;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import java.math.BigDecimal;

public class PayException extends Exception {
    private static final Logger LOGGER=Logger.getLogger(Main.class);
    public PayException(String msg){
        super(msg);
    }
    public PayException(BigDecimal sumTX,BigDecimal debtorAmount){
        super("sum to pay is less than debtor invertory ");
        LOGGER.debug("sum to pay is less than debtor invertory \n sum toPay= " + debtorAmount + " debtor invertory= " + sumTX);
    }
}

