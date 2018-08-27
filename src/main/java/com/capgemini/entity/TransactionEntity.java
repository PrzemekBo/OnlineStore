package com.capgemini.entity;

import com.capgemini.enums.Status;
import com.capgemini.listener.CreateListener;
import com.capgemini.listener.UpdateListener;
import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transaction")
@Data
@EntityListeners({ CreateListener.class, UpdateListener.class })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TransactionEntity extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull
    private Date transactionDate;

    @Column(nullable = false)
    @Enumerated
    private Status status;

    @Column(length = 40)
    @NotNull
    private Integer purchasesNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CustomerEntity customer;


    @ManyToMany
    private List<ProductEntity> products;
/*
    @Version
    private int version;*/



}
