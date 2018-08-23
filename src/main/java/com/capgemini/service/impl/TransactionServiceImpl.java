package com.capgemini.service.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dao.ProductDao;
import com.capgemini.dao.TransactionDao;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.entity.ProductEntity;
import com.capgemini.entity.TransactionEntity;
import com.capgemini.mapper.TransactionMapper;
import com.capgemini.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
      /*  TransactionEntity transactionEntity=transactionDao.save(TransactionMapper.toTransactionEntity(transactionDTO));
        return TransactionMapper.toTransactioDTO(transactionEntity);*/
        TransactionEntity transactionEntity = TransactionMapper.toTransactionEntity(transactionDTO);
        CustomerEntity customerEntity = customerDao.findCustomerEntityById(transactionDTO.getCustomer());
        transactionEntity.setCustomer(customerEntity);

        List<Long> productId = transactionDTO.getProducts();
        List<ProductEntity> listOfproduct = new LinkedList<>();

        for (Long id : productId) {
            listOfproduct.add(productDao.findProductEntityById(id));
        }

        transactionEntity.setProducts(listOfproduct);
        transactionDao.save(transactionEntity);
        addTransactionToCustomer(customerEntity, transactionEntity);
        addTransactionToProduct(listOfproduct, transactionEntity);
        return TransactionMapper.toTransactioDTO(transactionEntity);


    }



    @Override
    public void removeTransaction(Long id) {
        transactionDao.deleteById(id);

    }

    @Override
    public List<TransactionDTO> findAllTransactions() {
        return TransactionMapper.toTransactionTOList(transactionDao.findAll());
    }

    @Override
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = transactionDao.save(TransactionMapper.toTransactionEntity(transactionDTO));
        return TransactionMapper.toTransactioDTO(transactionEntity);
    }

    private void addTransactionToCustomer(CustomerEntity customerEntity, TransactionEntity transactionEntity) {
        List<TransactionEntity>transactions;

        if (customerEntity.getTransactions() !=null){
            transactions=customerEntity.getTransactions();
        }
        else{
            transactions= new LinkedList<>();
        }

        transactions.add(transactionEntity);
        customerEntity.setTransactions(transactions);
        customerDao.save(customerEntity);

    }

    private void addTransactionToProduct(List<ProductEntity>products, TransactionEntity transactionEntity) {

        List<TransactionEntity> transactions;



        for (ProductEntity productEntity:products){
            if (productEntity.getTransactions()!=null){
                transactions=productEntity.getTransactions();
            }else {
                transactions=new LinkedList<>();
            }
            transactions.add(transactionEntity);
            productEntity.setTransactions(transactions);


        }
        productDao.saveAll(products);
    }


    private void removeTransactionFromProducts(TransactionEntity transactionEntity, List<ProductEntity> productEntities) {

}
