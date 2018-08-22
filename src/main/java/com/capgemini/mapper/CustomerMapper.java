package com.capgemini.mapper;

import com.capgemini.dto.CustomerDTO;
import com.capgemini.entity.CustomerEntity;

import java.util.stream.Collectors;

public class CustomerMapper {

    public static CustomerEntity toCustomerEntity(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerDTO.getId());
        customerEntity.setFirstName(customerDTO.getFirstName());
        customerEntity.setLastName(customerDTO.getLastName());
        customerEntity.setEmail(customerDTO.getEmail());
        customerEntity.setPhoneNumber(customerDTO.getPhoneNumber());
        customerEntity.setAddress(customerDTO.getAddress());
        customerEntity.setBirthDate(customerDTO.getBirthDate());
        customerEntity.setVersion(customerDTO.getVersion());
        return customerEntity;

    }

    public static CustomerDTO toCustomerDTO(CustomerEntity customerEntity) {
        if (customerEntity == null) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerEntity.getId());
        customerDTO.setFirstName(customerEntity.getFirstName());
        customerDTO.setLastName(customerEntity.getLastName());
        customerDTO.setEmail(customerEntity.getEmail());
        customerDTO.setPhoneNumber(customerEntity.getPhoneNumber());
        customerDTO.setAddress(customerEntity.getAddress());
        customerDTO.setBirthDate(customerEntity.getBirthDate());
        customerDTO.setVersion(customerEntity.getVersion());

        if (customerEntity.getTransactions() != null) {
            customerDTO.setTransactions(customerEntity.getTransactions().stream().map(t->t.getId()).collect(Collectors.toList()));
        }
        return customerDTO;
    }

}