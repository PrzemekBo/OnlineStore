package com.capgemini.service.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dto.CustomerDTO;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.mapper.CustomerMapper;
import com.capgemini.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    @Override
    public CustomerDTO findCustomerEntityById(Long id) {
        return CustomerMapper.toCustomerDTO(customerDao.findCustomerEntityById(id));
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = customerDao.save(CustomerMapper.toCustomerEntity(customerDTO));
        return CustomerMapper.toCustomerDTO(customerEntity);
    }

    @Override
    public void removeCustomer(Long id) {
        customerDao.deleteById(id);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        CustomerEntity customerEntity = customerDao.save(CustomerMapper.toCustomerEntity(customer));
        return CustomerMapper.toCustomerDTO(customerEntity);

    }

    @Override
    public List<CustomerDTO> findThreeBestClientsInSomeTimePeriod(Date startDate, Date endDate) {
        return CustomerMapper.toClientTOList(customerDao.findThreeBestClientsInSomeTimePeriod(startDate, endDate));
    }


}
