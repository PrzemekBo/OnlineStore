package com.capgemini.entity;


import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
public class CustomerEntity {


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

    @OneToMany
    private List<TransactionEntity> transactions;
}
