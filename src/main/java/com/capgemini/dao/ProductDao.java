package com.capgemini.dao;

import com.capgemini.dao.custom.ProductDaoCustom;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends CrudRepository<ProductEntity, Long>, ProductDaoCustom {

    ProductEntity findProductEntityById(Long id);
}
