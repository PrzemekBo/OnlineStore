package com.capgemini.service;

import com.capgemini.dto.CustomerDTO;

import java.util.Date;
import java.util.List;

public interface CustomerService {

    CustomerDTO findCustomerEntityById(Long id);

    CustomerDTO addCustomer(CustomerDTO customerDTO);

    void removeCustomer(Long id);

    CustomerDTO updateCustomer(CustomerDTO customer);


    List<CustomerDTO> findThreeBestClientsInSomeTimePeriod(Date startDate, Date endDate);


}
