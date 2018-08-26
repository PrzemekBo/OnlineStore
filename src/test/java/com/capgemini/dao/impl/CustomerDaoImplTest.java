package com.capgemini.dao.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dao.ProductDao;
import com.capgemini.dto.CustomerDTO;
import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.enums.Status;
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
    private ProductDao productDao;

    @Autowired
    private CustomerDao customerDao;

    @Test
    @Transactional
    public void shouldFindThreeBestClientsInSomeTimePeriod() throws TooManyTheSameProductException, InvalidPropertiesFormatException {


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
                .productName("Torba")
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
         TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        TransactionDTO transaction2 = TransactionDTO.builder()
                .transactionDate(new Date(300L))
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction2 = transactionService.addTransaction(transaction);






      /*  product.setPrice(500L);
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
       // TransactionDTO newTransaction = transactionService.addTransaction(transaction);

        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        transaction.setTransactionDate(new Date(2000L));
        transaction.setCustomer(newCustomer2.getId());
        transactionService.addTransaction(transaction);



        transaction.setCustomer(newCustomer4.getId());
        transaction.setTransactionDate(new Date(3000L));
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);

        transaction.setCustomer(newCustomer3.getId());
        transaction.setTransactionDate(new Date(4000L));
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);


        transaction.setTransactionDate(new Date(1500L));
        productsList.clear();
        productsList.add(newProduct2.getId());
        transaction.setProducts(productsList);
        transactionService.addTransaction(transaction);


        //when
        List<CustomerEntity> clients = customerDao.findThreeBestClientsInSomeTimePeriod(new Date(500L), new Date(5500L));
        List<CustomerEntity> clients2 = customerDao.findThreeBestClientsInSomeTimePeriod(new Date(2500L), new Date(4500L));
        List<CustomerEntity> clients3 = customerDao.findThreeBestClientsInSomeTimePeriod(new Date(500L), new Date(3500L));
        List<CustomerEntity> clients0 = customerDao.findThreeBestClientsInSomeTimePeriod(new Date(500L), new Date(700L));

        assertThat(clients.size()).isEqualTo(3);
        assertThat(clients.get(0).getId()).isEqualTo(newCustomer3.getId());
        assertThat(clients2.size()).isEqualTo(2);
        assertThat(clients2.get(0).getId()).isEqualTo(newCustomer3.getId());
        assertThat(clients3.size()).isEqualTo(3);
        assertThat(clients3.get(0).getId()).isEqualTo(newCustomer4.getId());
        assertThat(clients0.size()).isEqualTo(0);
*/

    }
}