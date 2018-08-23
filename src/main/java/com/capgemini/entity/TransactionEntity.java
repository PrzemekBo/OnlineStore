package com.capgemini.entity;

import com.capgemini.enums.Status;
import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class TransactionEntity extends AbstractEntity implements Serializable {

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

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CustomerEntity customer;


    @ManyToMany
    private List<ProductEntity> products;

    @Version
    private int version;



}
