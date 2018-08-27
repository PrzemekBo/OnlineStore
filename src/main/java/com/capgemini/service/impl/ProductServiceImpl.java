package com.capgemini.service.impl;

import com.capgemini.dao.ProductDao;
import com.capgemini.dto.ProductDTO;

import com.capgemini.entity.ProductEntity;
import com.capgemini.mapper.ProductMapper;
import com.capgemini.service.ProductService;
import com.querydsl.core.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {

    ProductDao productDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao){this.productDao=productDao;}


    @Override
    public ProductDTO findProductEntityById(Long id) {
        return  ProductMapper.toProductDTO(productDao.findProductEntityById(id));

    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        ProductEntity productEntity=productDao.save(ProductMapper.toProductEntity(productDTO));
        return ProductMapper.toProductDTO(productEntity);
    }

    @Override
    public void removeProduct(Long id) {
        productDao.deleteById(id);

    }

    @Override
    public ProductDTO updateProduct(ProductDTO product) {
        ProductEntity productEntity= productDao.save(ProductMapper.toProductEntity(product));
        return ProductMapper.toProductDTO(productEntity);
    }

    @Override
    public List<ProductDTO> findTenBestSellers() {
        return ProductMapper.toProductTOList(productDao.findTenBestSellers());

    }


}
