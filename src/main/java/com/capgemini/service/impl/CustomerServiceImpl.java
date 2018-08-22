package com.capgemini.service.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dto.CustomerDTO;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.mapper.CustomerMapper;
import com.capgemini.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {



    CustomerDao customerDao;


    @Autowired
    CustomerServiceImpl(CustomerDao customerDao){this.customerDao=customerDao;}


    @Override
    public CustomerDTO findCustomerEntityById(Long id) {
        return CustomerMapper.toCustomerDTO(customerDao.findCustomerEntityById(id));
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity=customerDao.save(CustomerMapper.toCustomerEntity(customerDTO));
        return CustomerMapper.toCustomerDTO(customerEntity);
    }

    @Override
    public void removeCustomer(Long id) {
        customerDao.deleteById(id);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        CustomerEntity customerEntity=customerDao.save(CustomerMapper.toCustomerEntity(customer));
        return CustomerMapper.toCustomerDTO(customerEntity);
/*       CustomerEntity customerEntity=customerDao.findCustomerEntityById(customer.getId());
        customerEntity.setFirstName(customer.getFirstName());
        customerEntity.setLastName(customer.getLastName());
        customerEntity.setEmail(customer.getEmail());
        customerEntity.setPhoneNumber(customer.getPhoneNumber());
        customerEntity.setAddress(customer.getAddress());
        customerDao.save(customerEntity);
        return CustomerMapper.toCustomerDTO(customerEntity);*/
    }


}
