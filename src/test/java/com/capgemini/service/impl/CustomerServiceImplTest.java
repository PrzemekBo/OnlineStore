package com.capgemini.service.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dto.CustomerDTO;
import com.capgemini.dto.CustomerDTO.CustomerDTOBuilder;
import com.capgemini.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@SpringBootTest(properties = "spring.profiles.active=hsql")
@RunWith(SpringRunner.class)
public class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerDao customerDao;

    @Test
    @Transactional
    public void shouldAddCustomer() {

        //given
        CustomerDTO customer= new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email("adam.kowalski@wp.pl")
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .version(1)
                .build();
        CustomerDTO newCustomer=customerService.addCustomer(customer);

        //when
        CustomerDTO selectedClientCustomer = customerService.findCustomerEntityById(newCustomer.getId());

        //then
        assertThat(selectedClientCustomer.getId()).isEqualTo(newCustomer.getId());

    }


    @Test
    @Transactional
    public void shouldDeleteCustomer() {

        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email("adam.kowalski@wp.pl")
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .version(1)
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);

        //when
        customerService.removeCustomer(newCustomer.getId());

        //then
        assertThat(customerService.findCustomerEntityById(newCustomer.getId())).isNull();
    }

    @Test
    @Transactional
    public void shouldUpdateCustomer() {

        final String address ="Wroclaw" ;

        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email("adam.kowalski@wp.pl")
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);


        CustomerDTO cusumerToChangeAddress = customerService.findCustomerEntityById(newCustomer.getId());
        cusumerToChangeAddress.setAddress(address);
        customerService.updateCustomer(cusumerToChangeAddress);

        //then
        assertThat(customerService
                .findCustomerEntityById(cusumerToChangeAddress
                .getId()).getAddress())
                .isEqualTo(address);


    }

    @Test
    @Transactional
    public void shouldUpdateVersion() {

        final String address ="Wroclaw" ;

        //given
        CustomerDTO customer = new CustomerDTO().builder()
                .firstName("Adam")
                .lastName("Kowalski")
                .email("adam.kowalski@wp.pl")
                .phoneNumber("433545343")
                .address("Warszawa")
                .birthDate(new Date())
                .build();
        CustomerDTO newCustomer = customerService.addCustomer(customer);

        newCustomer.setAddress(address);
        int version= customerDao.findCustomerEntityById(newCustomer.getId()).getVersion();
        customerService.updateCustomer(newCustomer);
        int version2= customerDao.findCustomerEntityById(newCustomer.getId()).getVersion();

        assertThat(version).isNotEqualTo(version2);

    }

}