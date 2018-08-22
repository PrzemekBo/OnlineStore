package com.capgemini.service;

import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;

public interface TransactionService {

    TransactionDTO findTransactionEntityById(Long id);

    TransactionDTO addTransaction(TransactionDTO transactionDTO);

    void removeTransaction(Long id);

    TransactionDTO updateTransaction(TransactionDTO transactionDTO);
}
