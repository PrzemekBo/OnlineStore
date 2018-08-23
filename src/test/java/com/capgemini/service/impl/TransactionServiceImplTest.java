package com.capgemini.service.impl;

import com.capgemini.dto.CustomerDTO;
import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.enums.Status;
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
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(properties = "spring.profiles.active=hsql")
@RunWith(SpringRunner.class)
public class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;


    @Test
    @Transactional
    public void shouldAddTransaction() {

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
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        List<Long> listOfProducts = new LinkedList<>();
        listOfProducts.add(newProduct.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date())
                .status(Status.IN_DELIVERY)
                .products(listOfProducts)
                .purchasesNumber(listOfProducts.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);

        //when
        TransactionDTO transactionDTO = transactionService.findTransactionEntityById(newTransaction.getId());


        //then
        assertThat(transactionDTO.getId()).isEqualTo(transactionDTO.getId());
        assertThat(transactionDTO.getProducts().size()).isEqualTo(1);
        assertThat(customerService.findCustomerEntityById(newCustomer.getId()).getTransactions().size()).isEqualTo(1);
        assertThat(customerService.findCustomerEntityById(newCustomer.getId()).getTransactions().get(0)).isEqualTo(newTransaction.getId());
        assertThat(productService.findProductEntityById(newProduct.getId()).getTransactions().size()).isEqualTo(1);
        assertThat(productService.findProductEntityById(newProduct.getId()).getTransactions().get(0)).isEqualTo(newTransaction.getId());


    }



    @Test
    @Transactional
    public void shouldRemoveTransaction() {

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
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        List<Long> listOfProducts = new LinkedList<>();
        listOfProducts.add(newProduct.getId());

        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date())
                .status(Status.IN_DELIVERY)
                .products(listOfProducts)
                .purchasesNumber(listOfProducts.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        transactionService.removeTransaction(newTransaction.getId());
        List<TransactionDTO> transactions = transactionService.findAllTransactions();

        //then
        assertThat(transactions.size()).isEqualTo(0);
    }
}