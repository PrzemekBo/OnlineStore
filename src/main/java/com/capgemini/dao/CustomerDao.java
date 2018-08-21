package com.capgemini.dao;


import com.capgemini.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends CrudRepository<CustomerEntity, Long> {

    CustomerEntity findCustomerEntityById(Long id);
    CustomerEntity deleteCustomerEntityById(Long id);
 
}
