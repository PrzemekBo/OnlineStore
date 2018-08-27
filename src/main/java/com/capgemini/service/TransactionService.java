package com.capgemini.service;

import com.capgemini.criteria.TransactionSearchCriteria;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.TransactionEntity;
import com.capgemini.enums.Status;
import com.capgemini.exception.ToLargeWeightException;
import com.capgemini.exception.TooManyTheSameProductException;
import com.capgemini.exception.TransactionSearchCriteriaException;

import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;


public interface TransactionService {

    TransactionDTO findTransactionEntityById(Long id);

    TransactionDTO addTransaction(TransactionDTO transactionDTO) throws InvalidPropertiesFormatException, TooManyTheSameProductException, ToLargeWeightException;

    List<TransactionDTO> findAllTransactions();

    void removeTransaction(Long id);

    TransactionDTO updateTransaction(TransactionDTO transactionDTO);


    Long calculateProfitInSomeTimePeriod(Date startDate, Date endDate);

    Long sumAllPriceOfTransactionsForCustomer(Long id);

    Long calculateAllPriceOfTransactionsForCustomerByStatus(Long id, Status status);

    Long calculateAllPriceOfTransactionsForAllCustomersByStatus(Status status);

    List<TransactionEntity> searchTransactionByFourCriteria(TransactionSearchCriteria transactionSearchCriteria) throws TransactionSearchCriteriaException;


}
