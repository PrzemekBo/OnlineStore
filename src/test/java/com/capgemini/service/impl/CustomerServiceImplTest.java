package com.capgemini.service.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dto.CustomerDTO;
import com.capgemini.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

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

        final String address = "Wroclaw";

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

        //when
        CustomerDTO cusumerToChangeAddress = customerService.findCustomerEntityById(newCustomer.getId());
        cusumerToChangeAddress.setAddress(address);
        customerService.updateCustomer(cusumerToChangeAddress);

        CustomerDTO customerDTO = customerService.findCustomerEntityById(cusumerToChangeAddress.getId());

        //then
        assertThat(customerDTO.getAddress()).isEqualTo(address);


    }

    @Test
    @Transactional
    public void shouldUpdateVersion() {

        final String address = "Wroclaw";

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


        //when
        newCustomer.setAddress(address);
        int version = customerDao.findCustomerEntityById(newCustomer.getId()).getVersion();
        customerService.updateCustomer(newCustomer);
        int version2 = customerDao.findCustomerEntityById(newCustomer.getId()).getVersion();


        //then
        assertThat(version).isNotEqualTo(version2);

    }

    @Test(expected = OptimisticLockingFailureException.class)
    public void shouldTestOptimisticLookingExceptionForCustomer() {


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


        newCustomer.setFirstName("test1");
        customerService.updateCustomer(newCustomer);

        newCustomer.setFirstName("test2");
        customerService.updateCustomer(newCustomer);


    }

}