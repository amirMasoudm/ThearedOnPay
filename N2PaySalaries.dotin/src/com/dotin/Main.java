package com.dotin;



import com.dotin.dto.OprationType;
import com.dotin.dto.PayVO;

import com.dotin.repository.InvertoryRepository;
import com.dotin.repository.PayRepository;

import com.dotin.service.PayService;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) {
        InvertoryRepository invertoryRepository = new InvertoryRepository();
        PayRepository payRepository = new PayRepository();
        payRepository.genratePaymentFile();
        invertoryRepository.generateInventoryFile();
        List<PayVO> paymentList = payRepository.getPaymentFile();
//        //get inventory file will in Thread for synchroinzble

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        paymentList.forEach(payment -> {
            PayVO debtor = paymentList.stream().filter(findeDebtor -> findeDebtor.getOprationType().equals(OprationType.debtor)).findAny().get();//this difintly present but change no problem
            executorService.execute(() -> new PayService(payment, debtor).run());
        });
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }
}
