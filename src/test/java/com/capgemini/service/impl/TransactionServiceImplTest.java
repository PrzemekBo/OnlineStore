package com.capgemini.service.impl;

import com.capgemini.dao.ProductDao;
import com.capgemini.dao.TransactionDao;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.enums.Status;
import com.capgemini.service.ProductService;
import com.capgemini.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


@SpringBootTest(properties = "spring.profiles.active=hsql")
@RunWith(SpringRunner.class)
public class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionDao transactionDao;


    @Test
    @Transactional
    public void shouldAddTransaction() {

        TransactionDTO transaction= TransactionDTO.builder()
                .transactionDate(new Date())
                .status(Status.ANNULLED)
                .products()



        private Long id;
        private Date transactionDate;
        private Status status;
        private Long purchasesNumber;
        private List<Long> products;
        private int version;


    }
}