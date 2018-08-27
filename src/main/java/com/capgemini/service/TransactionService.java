package com.capgemini.service;

import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.exception.ToLargeWeightException;
import com.capgemini.exception.TooManyTheSameProductException;


import java.util.InvalidPropertiesFormatException;
import java.util.List;


public interface TransactionService {

    TransactionDTO findTransactionEntityById(Long id);

    TransactionDTO addTransaction(TransactionDTO transactionDTO) throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException;

    List<TransactionDTO> findAllTransactions();

    void removeTransaction(Long id);

    TransactionDTO updateTransaction(TransactionDTO transactionDTO);
}
