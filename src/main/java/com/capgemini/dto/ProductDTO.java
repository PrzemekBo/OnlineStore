package com.capgemini.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String productName;
    private Long price;
    private Long margin;
    private Long weight;
    private int version;
    private List<Long> transactions;

}
