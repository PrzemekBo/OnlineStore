package com.capgemini.dao;


import com.capgemini.dao.custom.CustomerDaoCustom;
import com.capgemini.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends CrudRepository<CustomerEntity, Long>, CustomerDaoCustom {

    CustomerEntity findCustomerEntityById(Long id);


}
