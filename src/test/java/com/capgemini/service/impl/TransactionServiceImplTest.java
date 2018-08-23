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
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


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
    public void shouldAddTransaction() throws InvalidPropertiesFormatException {

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
    public void shouldRemoveTransaction() throws InvalidPropertiesFormatException {

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


    @Test
    @Transactional
    public void shouldUpdateTransaction() throws InvalidPropertiesFormatException {


        final Status status = Status.EXECUTED;
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

        TransactionDTO transactionToUpdate = transactionService.findTransactionEntityById(newTransaction.getId());
        transactionToUpdate.setStatus(status);
        transactionService.updateTransaction(transactionToUpdate);

        //then
        assertThat(transactionService
                .findTransactionEntityById(transactionToUpdate.getId()).getStatus())
                .isEqualTo(status);


    }

    @Test
    @Transactional
    public void shouldFindTwoTransaction() throws InvalidPropertiesFormatException {

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
        TransactionDTO newTransaction2 = transactionService.addTransaction(transaction);

        //when
        List<TransactionDTO> transactions = transactionService.findAllTransactions();

        //then
        assertThat(transactions.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void shouldThromExceptionWhenPriceIsToBig() throws InvalidPropertiesFormatException {


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
                .price(5001L)
                .margin(10L)
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);
        ProductDTO newProduct2= productService.addProduct(product);



        List<Long> listOfProducts = new LinkedList<>();
        listOfProducts.add(newProduct.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date())
                .status(Status.IN_DELIVERY)
                .products(listOfProducts)
                .purchasesNumber(listOfProducts.size())
                .customer(newCustomer.getId())
                .build();

       //when
        boolean exceptionThrown = false;
        try {
            transactionService.addTransaction(transaction);
        } catch (InvalidPropertiesFormatException e){
            exceptionThrown = true;
        }

        //then
        assertTrue(exceptionThrown);
    }

    @Test
    @Transactional
    public void shouldAddTransactionWhenNumbersOfTransactionIsCorrect() throws InvalidPropertiesFormatException {


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
                .price(5001L)
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
        TransactionDTO transaction2 = TransactionDTO.builder()
                .transactionDate(new Date())
                .status(Status.IN_DELIVERY)
                .products(listOfProducts)
                .purchasesNumber(listOfProducts.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO transaction3 = TransactionDTO.builder()
                .transactionDate(new Date())
                .status(Status.IN_DELIVERY)
                .products(listOfProducts)
                .purchasesNumber(listOfProducts.size())
                .customer(newCustomer.getId())
                .build();

        TransactionDTO transaction4 = TransactionDTO.builder()
                .transactionDate(new Date())
                .status(Status.IN_DELIVERY)
                .products(listOfProducts)
                .purchasesNumber(listOfProducts.size())
                .customer(newCustomer.getId())
                .build();


        TransactionDTO newTransaction = transactionService.addTransaction(transaction);
        TransactionDTO newTransaction2 = transactionService.addTransaction(transaction2);
        TransactionDTO newTransaction3 = transactionService.addTransaction(transaction3);


        assertThat(customerService.findCustomerEntityById(newCustomer.getId()).getTransactions().size()).isEqualTo(3);

    }
}