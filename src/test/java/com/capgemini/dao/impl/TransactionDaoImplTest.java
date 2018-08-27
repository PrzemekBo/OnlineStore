package com.capgemini.dao.impl;

import com.capgemini.criteria.TransactionSearchCriteria;
import com.capgemini.dao.TransactionDao;
import com.capgemini.dto.CustomerDTO;
import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.TransactionEntity;
import com.capgemini.enums.Status;
import com.capgemini.exception.ToLargeWeightException;
import com.capgemini.exception.TooManyTheSameProductException;
import com.capgemini.exception.TransactionSearchCriteriaException;
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
public class TransactionDaoImplTest {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TransactionService transactionService;


    @Autowired
    private TransactionDao transactionDao;


    private static final Long START_DATE = 1000L;
    private static final Long END_DATE = 1500L;
    private static final Long PROFIT = 6000L;
    private static final Status IN_DELIVERY_STATUS = Status.IN_DELIVERY;
    private static final String TEST_EMAIL = "test123@gmail.com";


    @Test
    @Transactional
    public void shouldCalculateProfitInSomeTimePeriod() throws TooManyTheSameProductException, InvalidPropertiesFormatException, ToLargeWeightException {

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
                .price(2000L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        ProductDTO product2 = ProductDTO.builder()
                .productName("Plecak")
                .price(1000L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct2 = productService.addProduct(product2);

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


        Long profitInPeriodTime = transactionService.calculateProfitInSomeTimePeriod(new Date(START_DATE), new Date(END_DATE));

        assertThat(profitInPeriodTime).isEqualTo(PROFIT);

    }


    @Test
    @Transactional
    public void shouldSumAllPriceOfTransactionsForCustomer() throws TooManyTheSameProductException, InvalidPropertiesFormatException, ToLargeWeightException {

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
                .price(2000L)
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
                .status(IN_DELIVERY_STATUS)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);
        TransactionDTO newTransaction2 = transactionService.addTransaction(transaction);

        Long price = transactionService.sumAllPriceOfTransactionsForCustomer(newCustomer.getId());

        assertThat(price).isEqualTo(8000L);

    }

    @Test
    @Transactional
    public void shouldCalculateAllPriceOfTransactionsForCustomerByStatus() throws TooManyTheSameProductException, InvalidPropertiesFormatException, ToLargeWeightException {

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
                .price(2000L)
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


        Long cost = transactionService.calculateAllPriceOfTransactionsForCustomerByStatus(newCustomer.getId(), IN_DELIVERY_STATUS);


        assertThat(cost).isEqualTo(4000L);


        //TODO dkoncy y innym satusem

    }


    @Test
    @Transactional
    public void shouldCalculateAllPriceOfTransactionsForCustomerByStatusIfThisStatusDontExist() throws TooManyTheSameProductException, InvalidPropertiesFormatException, ToLargeWeightException {

        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowawlski")
                .email("adam.kowalski@wp.pl")
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);


        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(2000L)
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
                .status(Status.ANNULLED)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        Long cost = transactionService.calculateAllPriceOfTransactionsForCustomerByStatus(newCustomer.getId(), IN_DELIVERY_STATUS);


        assertThat(cost).isNull();

    }

    @Test
    @Transactional
    public void shouldCalculateAllPriceOfTransactionsForAllCustomersByStatus() throws TooManyTheSameProductException, InvalidPropertiesFormatException, ToLargeWeightException {
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

        CustomerDTO customer2 = new CustomerDTO().builder()
                .firstName("Bartek")
                .lastName("Kowalski")
                .email("adam.kowalski@wp.pl")
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer2 = customerService.addCustomer(customer);


        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(2000L)
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
                .transactionDate(new Date(1000L))
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer2.getId())
                .build();
        TransactionDTO newTransaction2 = transactionService.addTransaction(transaction);


        Long cost = transactionService.calculateAllPriceOfTransactionsForAllCustomersByStatus(IN_DELIVERY_STATUS);

        assertThat(cost).isEqualTo(8000L);
    }

    @Test
    @Transactional
    public void shouldFindTransactionByFourCriteria() throws TooManyTheSameProductException, InvalidPropertiesFormatException, TransactionSearchCriteriaException, ToLargeWeightException {
        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email(TEST_EMAIL)
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);

        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(2000L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        List<Long> productsList = new LinkedList<>();
        productsList.add(newProduct.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date(1000L))
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        TransactionSearchCriteria transactionSearchCriteria= new TransactionSearchCriteria();
        transactionSearchCriteria.setEmail(TEST_EMAIL);
        transactionSearchCriteria.setDateFrom(new Date(500L));
        transactionSearchCriteria.setDateTo(new Date(2000L));
        transactionSearchCriteria.setProductId(newProduct.getId());
        transactionSearchCriteria.setTrnsactionPrice(2000L);
        List<TransactionEntity> transactions = transactionService.searchTransactionByFourCriteria(transactionSearchCriteria);


        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.get(0).getCustomer().getEmail()).isEqualTo(TEST_EMAIL);

    }


    @Test
    @Transactional
    public void shouldFindTransactionByOneCriteria() throws TooManyTheSameProductException, InvalidPropertiesFormatException, TransactionSearchCriteriaException, ToLargeWeightException {
        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email(TEST_EMAIL)
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);

        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(2000L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        List<Long> productsList = new LinkedList<>();
        productsList.add(newProduct.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date(1000L))
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        TransactionSearchCriteria transactionSearchCriteria= new TransactionSearchCriteria();
        transactionSearchCriteria.setEmail(TEST_EMAIL);
        List<TransactionEntity> transactions = transactionService.searchTransactionByFourCriteria(transactionSearchCriteria);


        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.get(0).getCustomer().getEmail()).isEqualTo(TEST_EMAIL);

    }

    @Test
    @Transactional
    public void shouldFindTransactionByTwoCriteria() throws TooManyTheSameProductException, InvalidPropertiesFormatException, TransactionSearchCriteriaException, ToLargeWeightException {

        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email(TEST_EMAIL)
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);

        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(2000L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        List<Long> productsList = new LinkedList<>();
        productsList.add(newProduct.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date(1000L))
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        TransactionSearchCriteria transactionSearchCriteria= new TransactionSearchCriteria();
        transactionSearchCriteria.setEmail(TEST_EMAIL);
        transactionSearchCriteria.setTrnsactionPrice(2000L);
        List<TransactionEntity> transactions = transactionService.searchTransactionByFourCriteria(transactionSearchCriteria);


        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.get(0).getCustomer().getEmail()).isEqualTo(TEST_EMAIL);


    }

    @Test
    @Transactional
    public void shouldFindTransactionByThreeCriteria() throws TooManyTheSameProductException, InvalidPropertiesFormatException, TransactionSearchCriteriaException, ToLargeWeightException {
        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email(TEST_EMAIL)
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);

        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(2000L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        List<Long> productsList = new LinkedList<>();
        productsList.add(newProduct.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date(1000L))
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        TransactionSearchCriteria transactionSearchCriteria= new TransactionSearchCriteria();
        transactionSearchCriteria.setEmail(TEST_EMAIL);
        transactionSearchCriteria.setDateFrom(new Date(500L));
        transactionSearchCriteria.setDateTo(new Date(2000L));
        transactionSearchCriteria.setTrnsactionPrice(2000L);
        List<TransactionEntity> transactions = transactionService.searchTransactionByFourCriteria(transactionSearchCriteria);


        assertThat(transactions.size()).isEqualTo(1);
        assertThat(transactions.get(0).getCustomer().getEmail()).isEqualTo(TEST_EMAIL);

    }


    @Test
    @Transactional
    public void shouldFindThrowTransactionSearchCriteriaException() throws TooManyTheSameProductException, InvalidPropertiesFormatException, TransactionSearchCriteriaException, ToLargeWeightException {
        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Marek")
                .lastName("Kowalski")
                .email(TEST_EMAIL)
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);

        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(2000L)
                .margin(2L)
                .weight(2L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        List<Long> productsList = new LinkedList<>();
        productsList.add(newProduct.getId());


        TransactionDTO transaction = TransactionDTO.builder()
                .transactionDate(new Date(1000L))
                .status(Status.IN_DELIVERY)
                .products(productsList)
                .purchasesNumber(productsList.size())
                .customer(newCustomer.getId())
                .build();
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);


        TransactionSearchCriteria transactionSearchCriteria= new TransactionSearchCriteria();
        transactionSearchCriteria.setEmail(null);
        transactionSearchCriteria.setDateFrom(null);
        transactionSearchCriteria.setDateTo(null);
        transactionSearchCriteria.setProductId(null);
        transactionSearchCriteria.setTrnsactionPrice(null);
     //   List<TransactionEntity> transactions = transactionDao.searchTransactionByFourCriteria(transactionSearchCriteria);


        boolean exceptionThrown = false;
        try {
            transactionDao.searchTransactionByFourCriteria(transactionSearchCriteria);
        }catch (TransactionSearchCriteriaException e){
            exceptionThrown=true;
        }

        assertTrue(exceptionThrown);

    }



}