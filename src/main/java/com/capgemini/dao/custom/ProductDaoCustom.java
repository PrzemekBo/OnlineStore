package com.capgemini.dao.custom;
import com.capgemini.entity.ProductEntity;
import com.querydsl.core.Tuple;

import java.util.List;

public interface ProductDaoCustom {

    List<ProductEntity> findTenBestSellers();

    List<Tuple> findItemsInInDeliveryStatus();

}
