package com.capgemini.service;

import com.capgemini.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO findProductEntityById(Long id);

    ProductDTO addProduct(ProductDTO productDTO);

    void removeProduct(Long id);

    ProductDTO updateProduct(ProductDTO product);

    List<ProductDTO> findTenBestSellers();


}
