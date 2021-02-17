package com.dotin.service;

import com.dotin.dto.InvertoryVO;
import com.dotin.dto.PayVO;
import com.dotin.repository.InvertoryRepository;
import com.dotin.repository.PayRepository;

import java.util.List;

public class PayService implements Runnable {
    InvertoryRepository invertoryRepository = new InvertoryRepository();
    final List<InvertoryVO> invertoryVOFile;
    private PayService (){
        invertoryVOFile   = invertoryRepository.findInventoryFile();
    }
    public static PayService payService=new PayService();
    public static PayService getInstance(){
        return payService;

    }

    @Override
    public void run() {

        invertoryRepository.generateInventoryFile();
        PayRepository payRepository = new PayRepository();
        payRepository.genratePaymentFile();

        List<PayVO> getPaymentFile = payRepository.getPaymentFile();

        synchronized (invertoryVOFile) {
         payRepository.paySalaries(getPaymentFile, invertoryVOFile);
        }
    }
}
