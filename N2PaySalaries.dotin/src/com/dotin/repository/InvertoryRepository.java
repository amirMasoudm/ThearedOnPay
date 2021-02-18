package com.dotin.repository;

import com.dotin.dto.InvertoryVO;

import org.apache.log4j.Logger;
import com.dotin.Main;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class InvertoryRepository {


    Path path = Paths.get("hear Type to set your transaction file Address");
    private final Logger LOOGER = Logger.getLogger(Main.class);


    protected InvertoryVO findInventory(String depositNumber, List<InvertoryVO> invertoryArray) {
        InvertoryVO invertoryVO = null;
        for (int i = 2; i < invertoryArray.size(); i += 2) {
            if (depositNumber.equals(invertoryArray.get(i).getDepositNumber())) {
                invertoryVO = invertoryArray.get(i);
            }
        }
        return invertoryVO;
    }

    public List<InvertoryVO> findInventoryFile() {
        List<String> inventoryFile;
        try {
            String rootPath = String.valueOf(path.getRoot());
            if (rootPath.equals("null")) {
                path = Paths.get(System.getProperty("user.home") + "\\invertory.txt");
            }
            Stream<String> lines = Files.lines(path);
            String invertoryContent = lines.collect(Collectors.joining("\t"));
            inventoryFile = Arrays.asList(invertoryContent.split("\t"));

            return convertInventoryArrayToObjectivList(inventoryFile);
        } catch (IOException d) {
            LOOGER.error(Thread.currentThread().getId()+"find file has problem ,the message::" + d.getMessage() + " dose not exite");
            path = Paths.get(System.getProperty("user.home") + "\\invertory.txt");
            return generateInventoryFile();
        }

    }

    private List<InvertoryVO> convertInventoryArrayToObjectivList(List<String> invertoryFiel) {
        List<InvertoryVO> invertoryVOList = new ArrayList<>();
        for (int i = 0; i < invertoryFiel.size(); i += 2) {
            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(invertoryFiel.get(i + 1)));
            InvertoryVO invertoryVO = new InvertoryVO(invertoryFiel.get(i), amount);
            invertoryVOList.add(invertoryVO);
        }
        return invertoryVOList;
    }

    public List<InvertoryVO> generateInventoryFile() {
        if (!Files.isExecutable(path)) {
            Set<String> arr = new LinkedHashSet<>();
            for (int i = 0; i < 1000; i++) {
                arr.add(generateRandomDepositNumber());
                if (arr.size() < i) i--;
            }
            List<InvertoryVO> invertoryVOList = new ArrayList<>();
            for (String depositNumber : arr) {
                BigDecimal inveAmount = generateRandomAmount();
                InvertoryVO invertoryVO = new InvertoryVO(depositNumber, inveAmount);
                insertToInventoryFile(invertoryVO);
                invertoryVOList.add(invertoryVO);
            }
            return invertoryVOList;
        } else return findInventoryFile();
    }

    private void insertToInventoryFile(InvertoryVO invertoryVO) {
        try {
            Files.write(path, invertoryVO.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            path = Paths.get(System.getProperty("user.home") + "\\invertory.txt");
            try {
                Files.write(path, invertoryVO.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException ioException) {
                LOOGER.fatal(ioException.getMessage());
            }
        }
    }

    protected void updateInventories(List<InvertoryVO> invertoryList) {
        List<InvertoryVO> invenoryArray = findInventoryFile();
        for (InvertoryVO lasteInvertory : invenoryArray) {
            for (InvertoryVO newInvertory : invertoryList) {
                if (lasteInvertory.getDepositNumber().equals(newInvertory.getDepositNumber())) {
                    lasteInvertory.setAmount(newInvertory.getAmount());
                }
            }
        }
        LOOGER.debug("invertoryFile Updated");
        insertInvertoryArray(invenoryArray);
    }

    private void insertInvertoryArray(List<InvertoryVO> invertoryArray) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            LOOGER.error("insert to invertory Array Afret got it, has problem!!the  message::" + e.getMessage());
        }
        for (InvertoryVO invertoryVO : invertoryArray) {
            insertToInventoryFile(invertoryVO);
        }
    }


    protected String generateRandomDepositNumber() {
        final String firestDN = "1.10.";
        Random r = new Random();
        int low = 0, high = 1000;
        int result = r.nextInt(high - low) + low;
        high = 200;
        int secondResult = new Random().nextInt(high - low) + low;
        return firestDN + result + "." + secondResult;
    }

    protected BigDecimal generateRandomAmount() {
        BigDecimal amount = BigDecimal.valueOf(new Random().nextDouble() * 10000);
        amount = amount.setScale(2, RoundingMode.HALF_DOWN);
        LOOGER.debug("normaly amount >>>>" + amount);
        return amount;
    }

    //its less return less than upper method
    protected BigDecimal generateRandomAmountTX() {
        BigDecimal amount = BigDecimal.valueOf(new Random().nextDouble() * 100);
        amount = amount.setScale(2, RoundingMode.HALF_DOWN);
        LOOGER.debug("amount TX>>>" + amount);
        return amount;
    }
}
