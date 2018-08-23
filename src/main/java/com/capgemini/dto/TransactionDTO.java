package com.capgemini.dto;

import com.capgemini.entity.ProductEntity;
import com.capgemini.enums.Status;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private Long id;
    private Date transactionDate;
    private Status status;
    private Integer purchasesNumber;
    private List<Long> products;
    private Long customer;
    private int version;

}
