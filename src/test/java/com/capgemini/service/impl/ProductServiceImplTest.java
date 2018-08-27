package com.capgemini.service.impl;

import com.capgemini.dao.ProductDao;
import com.capgemini.dto.ProductDTO;
import com.capgemini.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=hsql")
@RunWith(SpringRunner.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;


    @Test
    @Transactional
    public void shouldAddProduct() {


        //given
        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(5L)
                .margin(10L)
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);

        //when
        ProductDTO addedProduct = productService.findProductEntityById(newProduct.getId());

        //then
        assertThat(addedProduct.getId()).isEqualTo(newProduct.getId());

    }

    @Test
    @Transactional
    public void shouldRemoveProduct() {

        //given
        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(5L)
                .margin(10L)
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);

        //when
        productService.removeProduct(newProduct.getId());

        //then
        assertThat(productService.findProductEntityById(newProduct.getId())).isNull();
    }

    @Test
    @Transactional
    public void shouldUpdateProduct() {

        final Long weight = 15L;

        //given
        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(5L)
                .margin(10L)
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        //when
        ProductDTO productToChangeWeight = productService.findProductEntityById(newProduct.getId());
        productToChangeWeight.setWeight(weight);
        productService.updateProduct(productToChangeWeight);


        //then
        assertThat(productService
                .findProductEntityById(productToChangeWeight.getId())
                .getWeight())
                .isEqualTo(weight);

    }

    @Test(expected = OptimisticLockingFailureException.class)
    public void shouldTestOptimisticLookingExceptionForProduct() {

        ProductDTO product = ProductDTO.builder()
                .productName("Torba")
                .price(5L)
                .margin(10L)
                .weight(10L)
                .build();
        ProductDTO newProduct = productService.addProduct(product);


        newProduct.setPrice(2L);
        productService.updateProduct(newProduct);

        newProduct.setPrice(3L);
        productService.updateProduct(newProduct);


    }


}