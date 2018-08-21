package com.capgemini.entity;

import com.capgemini.enums.Status;
import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull
    private Date transactionDate;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 40)
    @NotNull
    private Long purchasesNumber;


    @ManyToMany
    private List<ProductEntity> products;



}
