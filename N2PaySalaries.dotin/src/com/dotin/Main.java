package com.dotin;


import com.dotin.dto.PayVO;
import com.dotin.repository.InvertoryRepository;
import com.dotin.repository.PayRepository;

import com.dotin.service.PayService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args)  {
InvertoryRepository invertoryRepository=new InvertoryRepository();
        PayRepository payRepository=new PayRepository();
        payRepository.genratePaymentFile();
        invertoryRepository.generateInventoryFile();
        List<PayVO> getPaymentFile=payRepository.getPaymentFile();
        //get inventory file will in Thread for synchroinzble

        ExecutorService executorService= Executors.newFixedThreadPool(2);
        PayService runnable=new PayService(getPaymentFile);
        executorService.execute(runnable);

    }
}
