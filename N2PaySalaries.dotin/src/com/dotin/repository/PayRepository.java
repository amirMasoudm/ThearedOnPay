package com.dotin.repository;

import com.dotin.dto.InvertoryVO;
import com.dotin.dto.OprationType;
import com.dotin.dto.PayVO;
import com.dotin.dto.TransactionVO;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PayRepository {
    private final Logger LOGGER = Logger.getLogger(PayRepository.class);
    private Path path = Paths.get("hear Type to set your transaction file Address");
    InvertoryRepository invertoryRepository = new InvertoryRepository();


    public void paySalaries(PayVO payment, PayVO debtor, List<InvertoryVO> inventoryFile) {

        if (payment.getOprationType().equals(OprationType.debtor)) {
            BigDecimal sumTX = payment.getAmount();
            InvertoryVO debtotrinventory = invertoryRepository.findInventory(payment.getDepositNumber(), inventoryFile);
            if (debtotrinventory.getAmount().compareTo(payment.getAmount()) >= 0) {
                invertoryRepository.updateInventories(payment);
            } else {
                try {
                    throw new PayException(sumTX, debtotrinventory.getAmount());
                } catch (PayException e) {
                    System.out.println("pay salaries failed !! becuas sum of salaries grater than debtor amount inventory");
                }
            }
        } else if (payment.getOprationType().equals(OprationType.creditor)) {
            invertoryRepository.updateInventories(payment);
            TransactionVO transactionVO = new TransactionVO(debtor.getDepositNumber(), payment.getDepositNumber(), payment.getAmount());
            TXRepository.getInstance().insertToTXFile(transactionVO);
        }

    }



    public List<PayVO> genratePaymentFile() {
        List<PayVO> payList = new ArrayList<>();
        String debtorDepositNumber = invertoryRepository.generateRandomDepositNumber();
        InvertoryVO debtorInvertory = invertoryRepository.findInventory(debtorDepositNumber, invertoryRepository.findInventoryFile());
        BigDecimal sumTX = new BigDecimal(0);
        sumTX = sumTX.setScale(2, RoundingMode.HALF_DOWN);
        if (debtorInvertory != null) {
            for (int i = 0; i < 10; i++) {
                String creditorDepositNumber = invertoryRepository.generateRandomDepositNumber();
                InvertoryVO creditorInvetory = invertoryRepository.findInventory(creditorDepositNumber, invertoryRepository.findInventoryFile());
                if (creditorInvetory != null) {
                    BigDecimal amount = invertoryRepository.generateRandomAmountTX();
                    amount = amount.setScale(2, RoundingMode.HALF_DOWN);
                    PayVO creitorPay = new PayVO(OprationType.creditor, creditorDepositNumber, amount);
                    payList.add(creitorPay);
                    sumTX = sumTX.add(amount);
                } else {
                    i--;
                }
            }
            PayVO debtroPay = new PayVO(OprationType.debtor, debtorDepositNumber, sumTX);
            payList.add(debtroPay);
            insertToPayFile(payList);
            return payList;
        } else return genratePaymentFile();
    }

    private void insertToPayFile(List<PayVO> payList) {
        Path checkpath = Paths.get(System.getProperty("user.home") + "\\pay.txt");
        try {

            Files.deleteIfExists(path);
            Files.deleteIfExists(checkpath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PayVO a = payList.get(0);
        payList.set(0, payList.get(payList.size() - 1));
        payList.set(payList.size() - 1, a);
        for (PayVO pay : payList) {
            insertToPayFle(pay);
        }
    }

    private void insertToPayFle(PayVO payVO) {
        payVO.setAmount(payVO.getAmount().setScale(2, RoundingMode.HALF_DOWN));
        try {
            String rootPath = String.valueOf(path.getRoot());
            if (rootPath.equals("null")) {
                path = Paths.get(System.getProperty("user.home") + "\\pay.txt");
            }
            Files.write(path, payVO.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            path = Paths.get(System.getProperty("user.home") + "\\pay.txt");
            try {
                Files.write(path, payVO.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException ioException) {
                LOGGER.fatal(ioException.getMessage());
            }

        }

    }

    public List<PayVO> getPaymentFile() {
        Stream<String> lines = null;
        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            path = Paths.get(System.getProperty("user.home") + "\\pay.txt");
            try {
                lines = Files.lines(path);
            } catch (IOException ioException) {
                LOGGER.fatal("its wrong!!");
            }
        }
        assert lines != null;
        String content = lines.collect(Collectors.joining("\t"));
        List<String> payArray = Arrays.asList(content.split("\t"));
        List<PayVO> payList = new ArrayList<>();
        for (int i = 0; i < payArray.size(); i += 3) {
            OprationType oprationType = OprationType.valueOf(payArray.get(i));
            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(payArray.get(i + 2)));
            PayVO payVO = new PayVO(oprationType, payArray.get(i + 1), amount);
            payList.add(payVO);
        }
        return payList;
    }

}

