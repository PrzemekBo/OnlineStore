package com.capgemini.dao;

import com.capgemini.entity.CustomerEntity;
import com.capgemini.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDao extends CrudRepository<TransactionEntity, Long> {

    TransactionEntity findTransactionEntityById(Long id);



}
