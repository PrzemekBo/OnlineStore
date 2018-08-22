package com.capgemini.service;

import com.capgemini.dto.CustomerDTO;
import com.capgemini.entity.CustomerEntity;

public interface CustomerService {

    CustomerDTO findCustomerEntityById(Long id);

    CustomerDTO addCustomer(CustomerDTO customerDTO);

    void removeCustomer(Long id);

    CustomerDTO updateCustomer(CustomerDTO customer);


}
