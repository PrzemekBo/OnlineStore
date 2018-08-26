package com.capgemini.entity;


import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
public class CustomerEntity extends AbstractEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 40)
    @NotNull
    private String firstName;

    @Column(length = 40)
    @NotNull
    private String lastName;

    @Column(length = 40)
    @NotNull
    private String email;

    @Column(length = 12)
    @NotNull
    private String phoneNumber;

    @Column(length = 120)
    @NotNull
    private String address;

    @Column
    @NotNull
    private Date birthDate;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<TransactionEntity> transactions;

 /*   @Version
    private int version;*/
}
