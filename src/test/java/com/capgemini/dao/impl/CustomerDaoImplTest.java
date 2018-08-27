package com.capgemini.dao.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dao.ProductDao;
import com.capgemini.dto.CustomerDTO;
import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.enums.Status;
import com.capgemini.exception.ToLargeWeightException;
import com.capgemini.exception.TooManyTheSameProductException;
import com.capgemini.service.CustomerService;
import com.capgemini.service.ProductService;
import com.capgemini.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


@SpringBootTest(properties = "spring.profiles.active=hsql")
@RunWith(SpringRunner.class)
public class CustomerDaoImplTest {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    CustomerDao customerDao;


    @Test
    @Transactional
    public void shouldFindThreeBestClientsInSomeTimePeriod() throws TooManyTheSameProductException, InvalidPropertiesFormatException, ToLargeWeightException {


        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email("adam.kowalski@wp.pl")
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);
        CustomerDTO newCustomer2 = customerService.addCustomer(customer);
        CustomerDTO newCustomer3 = customerService.addCustomer(customer);
        CustomerDTO newCustomer4 = customerService.addCustomer(customer);





        ProductDTO product = ProductDTO.builder()
                .productName("Plecak")
                .price(100L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);



        ProductDTO product2 = ProductDTO.builder()
                .productName("Torba")
                .price(2000L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct2 = productService.addProduct(product);

        List<Long> productsList = new LinkedList<>();
        productsList.add(newProduct.getId());
        productsList.add(newProduct2.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date(1000L))
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();


        transaction.setTransactionDate(new Date(300L));
        transaction.setCustomer(newCustomer.getId());
        transactionService.addTransaction(transaction);


        transaction.setTransactionDate(new Date(300L));
        transaction.setCustomer(newCustomer2.getId());
        transactionService.addTransaction(transaction);


        transaction.setTransactionDate(new Date(300L));
        transaction.setCustomer(newCustomer3.getId());
        transactionService.addTransaction(transaction);

        transaction.setTransactionDate(new Date(550L));
        transaction.setCustomer(newCustomer3.getId());
        transactionService.addTransaction(transaction);

        //given
        List<CustomerDTO> listCusomers = customerService.findThreeBestClientsInSomeTimePeriod(new Date(100L), new Date(600L));
        List<CustomerDTO> listCusomers2 = customerService.findThreeBestClientsInSomeTimePeriod(new Date(500L), new Date(600L));

        //then
        assertThat(listCusomers.size()).isEqualTo(3);
        assertThat(listCusomers.get(0).getId()).isEqualTo(newCustomer3.getId());

        assertThat(listCusomers2.size()).isEqualTo(1);
        assertThat(listCusomers2.get(0).getId()).isEqualTo(newCustomer3.getId());


    }
}