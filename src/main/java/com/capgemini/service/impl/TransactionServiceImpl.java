package com.capgemini.service.impl;

import com.capgemini.dao.TransactionDao;
import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.TransactionEntity;
import com.capgemini.mapper.TransactionMapper;
import com.capgemini.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.ManyToOne;


@Service
public class TransactionServiceImpl implements TransactionService {

    TransactionDao transactionDao;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao){
        this.transactionDao=transactionDao;
    }

    @Override
    public TransactionDTO findTransactionEntityById(Long id) {
         return TransactionMapper.toTransactioDTO(transactionDao.findTransactionEntityById(id));
    }

    @Override
    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity=transactionDao.save(TransactionMapper.toTransactionEntity(transactionDTO));
        return TransactionMapper.toTransactioDTO(transactionEntity);
    }

    @Override
    public void removeTransaction(Long id) {
        transactionDao.deleteById(id);

    }

    @Override
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity=transactionDao.save(TransactionMapper.toTransactionEntity(transactionDTO));
        return TransactionMapper.toTransactioDTO(transactionEntity);
    }
}
