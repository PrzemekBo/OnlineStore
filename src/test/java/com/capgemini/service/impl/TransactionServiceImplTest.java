package com.capgemini.service.impl;

import com.capgemini.dto.CustomerDTO;
import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
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
import org.springframework.dao.OptimisticLockingFailureException;
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
    private CustomerService customerService;


    @Autowired
    private ProductService productService;


    private static final Long PRICE_MORE_GREATER_THAN_7001 = 7001L;


    @Test
    @Transactional
    public void shouldAddTransaction() throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {


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
    public void shouldUpdateTransaction() throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {


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
    public void shouldFindTwoTransaction() throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {

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
    public void shouldThromExceptionWhenPriceIsToBig() throws InvalidPropertiesFormatException, TooManyTheSameProductException {


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
        ProductDTO newProduct2 = productService.addProduct(product);


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
        } catch (InvalidPropertiesFormatException e) {
            exceptionThrown = true;
        } catch (ToLargeWeightException e) {
            e.printStackTrace();
        }

        //then
        assertTrue(exceptionThrown);
    }

    @Test
    @Transactional
    public void shouldAddTransactionWhenNumbersOfTransactionIsCorrect() throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {


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
                .price(5000L)
                .margin(10L)
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);

        product.setPrice(5001L);
        ProductDTO newProduct2 = productService.addProduct(product);


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
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        listOfProducts.add(newProduct2.getId());
        transaction.setProducts(listOfProducts);
        transactionService.addTransaction(transaction);

        assertThat(customerService.findCustomerEntityById(newCustomer.getId()).getTransactions().size()).isEqualTo(4);

    }


    @Test
    @Transactional
    public void shouldRemoveTransaction() throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {

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
    public void shouldThrowExceptionWhenCusomerBuyTooManyTheSameProduct() throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {

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
                .price(5000L)
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


        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        transactionService.addTransaction(transaction);


        product.setPrice(PRICE_MORE_GREATER_THAN_7001);
        ProductDTO newProduct2 = productService.addProduct(product);

        listOfProducts.add(newProduct2.getId());
        listOfProducts.add(newProduct2.getId());
        listOfProducts.add(newProduct2.getId());
        listOfProducts.add(newProduct2.getId());
        listOfProducts.add(newProduct2.getId());
        listOfProducts.add(newProduct2.getId());

        transaction.setProducts(listOfProducts);


        //when
        boolean exceptionThrown = false;
        try {
            transactionService.addTransaction(transaction);
        } catch (TooManyTheSameProductException e) {
            exceptionThrown = true;
        }

        //then
        assertTrue(exceptionThrown);

    }

    @Test
    @Transactional
    public void shouldThrowExceptionToLargeWeightException() throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {

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
                .price(5000L)
                .margin(10L)
                .weight(26L)
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
        transaction.setProducts(listOfProducts);

        //when
        boolean exceptionThrown = false;
        try {
            transactionService.addTransaction(transaction);
        } catch (ToLargeWeightException e) {
            exceptionThrown = true;
        }

        //then
        assertTrue(exceptionThrown);

    }

    @Test
    @Transactional
    public void shouldAcceptPransactionWithLimitWeight() throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException {

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
                .price(5000L)
                .margin(10L)
                .weight(25L)
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


        TransactionDTO transactionDTO = transactionService.findTransactionEntityById(newTransaction.getId());


        assertThat(transactionDTO.getProducts().size()).isEqualTo(1);
        assertThat(productService.findProductEntityById(newProduct.getId()).getWeight()).isEqualTo(25L);

    }


    @Test(expected = OptimisticLockingFailureException.class)
    public void shouldTestOptimisticLookingExceptionForTransaction() throws TooManyTheSameProductException, ToLargeWeightException, InvalidPropertiesFormatException {


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
                .price(5000L)
                .margin(10L)
                .weight(25L)
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


        newTransaction.setStatus(Status.IN_IMPLEMENTATION);
        transactionService.updateTransaction(newTransaction);


        newTransaction.setStatus(Status.IN_DELIVERY);
        transactionService.updateTransaction(newTransaction);


    }


}