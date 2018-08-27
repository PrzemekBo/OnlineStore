package com.capgemini.mapper;

import com.capgemini.dto.ProductDTO;
import com.capgemini.entity.ProductEntity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductEntity toProductEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productDTO.getId());
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setMargin(productDTO.getMargin());
        productEntity.setWeight(productDTO.getWeight());
        productEntity.setVersion(productDTO.getVersion());
        return productEntity;


    }


    public static ProductDTO toProductDTO(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productEntity.getId());
        productDTO.setProductName(productEntity.getProductName());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setMargin(productEntity.getMargin());
        productDTO.setWeight(productEntity.getWeight());
        productDTO.setVersion(productDTO.getVersion());

        if (productEntity.getTransactions() != null) {
            productDTO.setTransactions(productEntity.getTransactions().stream().map(t -> t.getId()).collect(Collectors.toList()));
        }
        return productDTO;
    }


    public static List<ProductDTO> toProductTOList(Iterable<ProductEntity> products) {
        Iterator<ProductEntity> it = products.iterator();
        List<ProductDTO> productsDTO = new LinkedList<>();
        while (it.hasNext()) {
            productsDTO.add(toProductDTO(it.next()));
        }

        return productsDTO;
    }
}
