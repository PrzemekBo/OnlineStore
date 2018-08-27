package com.capgemini.dao.custom;

import com.capgemini.entity.CustomerEntity;

import java.util.Date;
import java.util.List;

public interface CustomerDaoCustom {

    List<CustomerEntity> findThreeBestClientsInSomeTimePeriod(Date startDate, Date endDate);

}
