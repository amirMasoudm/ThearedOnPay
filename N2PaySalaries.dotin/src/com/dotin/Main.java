package com.dotin;

import com.dotin.dto.InvertoryVO;
import com.dotin.dto.PayVO;
import com.dotin.repository.InvertoryRepository;
import com.dotin.repository.PayRepository;
import com.dotin.service.PayService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        InvertoryRepository invertoryRepository=new InvertoryRepository();
        PayRepository payRepository=new PayRepository();

        invertoryRepository.generateInventoryFile();
        payRepository.genratePaymentFile();

        List<PayVO> getPaymentFile=payRepository.getPaymentFile();
        List<InvertoryVO> getInvertoryFile=invertoryRepository.findInventoryFile();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
              synchronized (getInvertoryFile){
                  payRepository.paySalaries(getPaymentFile,getInvertoryFile);
              }
            }
        };
        Runnable runnable1=new Runnable() {
            @Override
            public void run() {
                synchronized (getInvertoryFile){
                    payRepository.paySalaries(getPaymentFile,getInvertoryFile);
                }
            }
        };

        ExecutorService executorService= Executors.newFixedThreadPool(2);
        executorService.execute(runnable);
        executorService.execute(runnable1);

//        Runnable runnable=PayService.getInstance();
//        PayService runnable1=PayService.getInstance();
//        executorService.execute(runnable);
//        executorService.execute(runnable1);


    }
}
