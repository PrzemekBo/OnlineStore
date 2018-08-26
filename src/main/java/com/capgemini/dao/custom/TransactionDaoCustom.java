package com.capgemini.dao.custom;

import com.capgemini.enums.Status;

import java.util.Date;

public interface TransactionDaoCustom {


    Long calculateProfitInSomeTimePeriod(Date startDate, Date endDate);

    Long sumAllPriceOfTransactionsForCustomer(Long id);

    Long calculateAllPriceOfTransactionsForCustomerByStatus(Long id, Status status);





}
