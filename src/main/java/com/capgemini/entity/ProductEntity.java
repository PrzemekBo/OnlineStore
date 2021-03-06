package com.capgemini.entity;

import com.capgemini.listener.CreateListener;
import com.capgemini.listener.UpdateListener;
import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@EntityListeners({CreateListener.class, UpdateListener.class})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ProductEntity extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(length = 40)
    @NotNull
    private String productName;

    @Column(length = 40)
    @NotNull
    private Long price;

    @Column(length = 40)
    @NotNull
    private Long margin;

    @Column(length = 40)
    @NotNull
    private Long weight;


    @ManyToMany
    private List<TransactionEntity> transactions;



}
