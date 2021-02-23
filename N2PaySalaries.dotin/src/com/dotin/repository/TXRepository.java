package com.dotin.repository;

import com.dotin.dto.TransactionVO;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class TXRepository {
    private final Logger LOOGER = Logger.getLogger(TXRepository.class);
    private Path path = Paths.get("hear Type to set your transaction file Address");
    private static final TXRepository txRepository = new TXRepository();

    public static TXRepository getInstance() {
        return txRepository;
    }

    public void insertToTXFile(TransactionVO transactionVO) {
        try {
            String rootPath = String.valueOf(path.getRoot());
            if (rootPath.equals("null")) {
                path = Paths.get(System.getProperty("user.home") + "\\transaction.txt");
            }
            Files.write(path, transactionVO.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            LOOGER.debug("transaction added");
        } catch (IOException e) {
            path = Paths.get(System.getProperty("user.home") + "\\transaction.txt");
            LOOGER.debug("insert to TxFaile has problem!! message::" + e.getMessage());
        }
    }

}
