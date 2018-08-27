package com.capgemini.dao.custom;

import com.capgemini.criteria.TransactionSearchCriteria;
import com.capgemini.entity.TransactionEntity;
import com.capgemini.enums.Status;
import com.capgemini.exception.TransactionSearchCriteriaException;

import java.util.Date;
import java.util.List;

public interface TransactionDaoCustom {


    Long calculateProfitInSomeTimePeriod(Date startDate, Date endDate);

    Long sumAllPriceOfTransactionsForCustomer(Long id);

    Long calculateAllPriceOfTransactionsForCustomerByStatus(Long id, Status status);

    Long calculateAllPriceOfTransactionsForAllCustomersByStatus(Status status);

    List<TransactionEntity> searchTransactionByFourCriteria(TransactionSearchCriteria transactionSearchCriteria) throws TransactionSearchCriteriaException;






}
