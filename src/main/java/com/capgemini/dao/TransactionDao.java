package com.capgemini.dao;

import com.capgemini.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface TransactionDao extends CrudRepository<CustomerEntity, Long> {
}
