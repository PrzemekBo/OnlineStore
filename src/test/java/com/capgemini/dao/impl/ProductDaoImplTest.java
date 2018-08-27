package com.capgemini.dao.impl;

import com.capgemini.dao.ProductDao;
import com.capgemini.dto.CustomerDTO;
import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.ProductEntity;
import com.capgemini.enums.Status;
import com.capgemini.exception.InvalidTransactionException;
import com.capgemini.exception.ToLargeWeightException;
import com.capgemini.exception.TooManyTheSameProductException;
import com.capgemini.service.CustomerService;
import com.capgemini.service.ProductService;
import com.capgemini.service.TransactionService;
import com.querydsl.core.Tuple;
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
public class ProductDaoImplTest {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductDao productDao;


    private static final Status IN_IMPLEMENTATION_STATUS=Status.IN_IMPLEMENTATION;


    @Test
    @Transactional
    public void shouldFindTenSellers() throws InvalidTransactionException, InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {

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


        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(5L)
                .margin(10L)
                .weight(1L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);
        ProductDTO newProduct2 = productService.addProduct(product);
        ProductDTO newProduct3 = productService.addProduct(product);
        ProductDTO newProduct4 = productService.addProduct(product);
        ProductDTO newProduct5 = productService.addProduct(product);
        ProductDTO newProduct6 = productService.addProduct(product);
        ProductDTO newProduct7 = productService.addProduct(product);
        ProductDTO newProduct8 = productService.addProduct(product);
        ProductDTO newProduct9 = productService.addProduct(product);
        ProductDTO newProduct10 = productService.addProduct(product);
        ProductDTO newProduct11 = productService.addProduct(product);


        List<Long> productsList = new LinkedList<>();
        productsList.add(newProduct.getId());
        productsList.add(newProduct2.getId());
        productsList.add(newProduct3.getId());
        productsList.add(newProduct4.getId());
        productsList.add(newProduct5.getId());
        productsList.add(newProduct6.getId());
        productsList.add(newProduct7.getId());
        productsList.add(newProduct8.getId());
        productsList.add(newProduct9.getId());
        productsList.add(newProduct10.getId());
        productsList.add(newProduct11.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date())
                .status(IN_IMPLEMENTATION_STATUS)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);
        transaction.setProducts(productsList);

        List<ProductEntity> items = productDao.findTenBestSellers();

        assertThat(items.size()).isEqualTo(10);

        //TODO sprawdyic cyz sa najwieksye dodane
    }




    @Test
    @Transactional
    public void shouldFindItemsInInplementationStatus() throws TooManyTheSameProductException, InvalidPropertiesFormatException, ToLargeWeightException {


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


        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(5L)
                .margin(10L)
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);
        ProductDTO newProduct2 = productService.addProduct(product);


        List<Long> productsList = new LinkedList<>();
        productsList.add(newProduct.getId());
        productsList.add(newProduct2.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date())
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
      //  TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);



        transaction.setCustomer(newCustomer2.getId());
        transaction.setStatus(IN_IMPLEMENTATION_STATUS);
        transactionService.addTransaction(transaction);

        //when
        List<Tuple> items = productDao.findItemsInInImplementationStatus();

        //then
        assertThat(items.size()).isEqualTo(2);

        //TODo moyna to troche poprawic
/*        assertThat(items.get(0).size()).isEqualTo(2);
        assertThat(items.get(0).toArray()[0]).isEqualTo(newProduct.getProductName());
        assertThat(items.get(0).toArray()[1]).isEqualTo(3L);
        assertThat(items.get(1).toArray()[0]).isEqualTo(newProduct2.getProductName());
        assertThat(items.get(1).toArray()[1]).isEqualTo(6L);*/
    }










}