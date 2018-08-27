package com.capgemini.service.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dao.ProductDao;
import com.capgemini.dao.TransactionDao;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.entity.ProductEntity;
import com.capgemini.entity.TransactionEntity;
import com.capgemini.exception.TooManyTheSameProductException;
import com.capgemini.mapper.TransactionMapper;
import com.capgemini.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;


@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionDao transactionDao;
    private ProductDao productDao;
    private CustomerDao customerDao;


    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao, CustomerDao customerDao, ProductDao productDao) {
        this.transactionDao = transactionDao;
        this.customerDao = customerDao;
        this.productDao = productDao;

    }

    @Override
    public TransactionDTO findTransactionEntityById(Long id) {
        return TransactionMapper.toTransactioDTO(transactionDao.findTransactionEntityById(id));
    }

    @Override
    public TransactionDTO addTransaction(TransactionDTO transactionDTO) throws InvalidPropertiesFormatException,TooManyTheSameProductException {

        TransactionEntity transactionEntity = TransactionMapper.toTransactionEntity(transactionDTO);
        CustomerEntity customerEntity = customerDao.findCustomerEntityById(transactionDTO.getCustomer());
        transactionEntity.setCustomer(customerEntity);

        List<Long> productId = transactionDTO.getProducts();
        List<ProductEntity> listOfproduct = new LinkedList<>();

        for (Long id : productId) {
            listOfproduct.add(productDao.findProductEntityById(id));
        }

        transactionEntity.setProducts(listOfproduct);
        transactionValidator(customerEntity,listOfproduct);
        transactionDao.save(transactionEntity);
        addTransactionToCustomer(customerEntity, transactionEntity);
        addTransactionToProduct(listOfproduct, transactionEntity);
        return TransactionMapper.toTransactioDTO(transactionEntity);
    }

    @Override
    public List<TransactionDTO> findAllTransactions() {
        return TransactionMapper.toTransactionTOList(transactionDao.findAll());
    }



    @Override
    public void removeTransaction(Long id) {
        TransactionEntity transactionEntity = transactionDao.findTransactionEntityById(id);

        CustomerEntity customerEntity = transactionEntity.getCustomer();
        removeTransactionFromCustomer(transactionEntity, customerEntity);

        List<ProductEntity> productEntities = transactionEntity.getProducts();
        removeTransactionFromProducts(transactionEntity, productEntities);

        transactionDao.deleteById(id);
    }

    @Override
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = transactionDao.save(TransactionMapper.toTransactionEntity(transactionDTO));
        return TransactionMapper.toTransactioDTO(transactionEntity);
    }

    private void addTransactionToCustomer(CustomerEntity customerEntity, TransactionEntity transactionEntity) {
        List<TransactionEntity> transactions;

        if (customerEntity.getTransactions() != null) {
            transactions = customerEntity.getTransactions();
        } else {
            transactions = new LinkedList<>();
        }

        transactions.add(transactionEntity);
        customerEntity.setTransactions(transactions);
        customerDao.save(customerEntity);

    }

    private void addTransactionToProduct(List<ProductEntity> products, TransactionEntity transactionEntity) {

        List<TransactionEntity> transactions;


        for (ProductEntity productEntity : products) {
            if (productEntity.getTransactions() != null) {
                transactions = productEntity.getTransactions();
            } else {
                transactions = new LinkedList<>();
            }
            transactions.add(transactionEntity);
            productEntity.setTransactions(transactions);


        }
        productDao.saveAll(products);
    }



    private void removeTransactionFromProducts(TransactionEntity transactionEntity, List<ProductEntity> productEntities) {
        for (ProductEntity product : productEntities) {
            List<TransactionEntity> transactionEntities = product.getTransactions();
            transactionEntities.remove(transactionEntity);
            product.setTransactions(transactionEntities);
        }
        productDao.saveAll(productEntities);
    }


    private void removeTransactionFromCustomer(TransactionEntity transactionEntity, CustomerEntity customerEntity) {
        List<TransactionEntity> customerTransactions = customerEntity.getTransactions();
        customerTransactions.remove(transactionEntity);
        customerEntity.setTransactions(customerTransactions);
        customerDao.save(customerEntity);
    }

    private void transactionValidator(CustomerEntity customerEntity, List<ProductEntity> products) throws InvalidPropertiesFormatException, TooManyTheSameProductException {
        checkIfClienthaveCorectNumberOfTransaction(customerEntity,products);
        checkIfTransactionHasMoreThanFiveProductWitchPriceAbove7000(products);

    }


    private void checkIfClienthaveCorectNumberOfTransaction(CustomerEntity customerEntity, List<ProductEntity> products) throws InvalidPropertiesFormatException {
            if (customerEntity.getTransactions()==null||customerEntity.getTransactions().size()<3){
                Long sumOfprice=0L;
                for (ProductEntity productEntity: products){
                    sumOfprice=productEntity.getPrice();
                }
                if (sumOfprice>5000){
                    throw new  InvalidPropertiesFormatException("You do not have enough transaction to buy for that amount");
                }

            }

    }

    private void checkIfTransactionHasMoreThanFiveProductWitchPriceAbove7000(List<ProductEntity> listOfProduct) throws TooManyTheSameProductException {
        List<ProductEntity> productsProductWitchPriceAbove7000 = new LinkedList<>();
        for (ProductEntity product : listOfProduct) {

            if (product.getPrice() > 7000L) {
                productsProductWitchPriceAbove7000.add(product);
            }

            for (int i = 0; i < productsProductWitchPriceAbove7000.size() - 1; i++) {
                int count = 1;
                for (int j = 1; j < productsProductWitchPriceAbove7000.size(); j++) {

                    if (productsProductWitchPriceAbove7000.get(i).getId() == productsProductWitchPriceAbove7000.get(j).getId()) {
                        count++;
                    }
                    if (count > 5) {
                        throw new TooManyTheSameProductException();
                    }
                }
            }
        }
    }

}
