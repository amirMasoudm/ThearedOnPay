package com.dotin.service;

import com.dotin.dto.InvertoryVO;
import com.dotin.dto.PayVO;
import com.dotin.repository.InvertoryRepository;
import com.dotin.repository.PayRepository;
import sun.util.resources.cldr.es.CalendarData_es_PY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PayService implements Runnable {

    PayVO peyment;
    PayVO debtor;
    public PayService(PayVO payment,PayVO debtor) {
        this.peyment=payment;
        this.debtor=debtor;

    }

    @Override
    public void run() {
        InvertoryRepository invertoryRepository = new InvertoryRepository();
        PayRepository payRepository = new PayRepository();
        List<InvertoryVO> invertoryVOFile = invertoryRepository.findInventoryFile();
        synchronized (invertoryRepository.findInventoryFile()) {
            payRepository.paySalaries(peyment,debtor, invertoryVOFile);
        }
    }
}
