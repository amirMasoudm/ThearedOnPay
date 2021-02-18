package com.dotin.service;

import com.dotin.dto.InvertoryVO;
import com.dotin.dto.PayVO;
import com.dotin.repository.InvertoryRepository;
import com.dotin.repository.PayRepository;

import java.util.List;

public class PayService implements Runnable {

    List<PayVO> getPaymentFile;
    public PayService(List<PayVO> getPaymentFile){
       this.getPaymentFile=getPaymentFile;
    }
    @Override
    public void run() {
        InvertoryRepository invertoryRepository = new InvertoryRepository();
        PayRepository payRepository = new PayRepository();
        List<InvertoryVO>  invertoryVOFile = invertoryRepository.findInventoryFile();
        synchronized (invertoryVOFile){
                payRepository.paySalaries(getPaymentFile, invertoryVOFile);
        }
    }
}
