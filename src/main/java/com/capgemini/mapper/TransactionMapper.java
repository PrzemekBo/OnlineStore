package com.capgemini.mapper;

import com.capgemini.dto.CustomerDTO;
import com.capgemini.dto.ProductDTO;
import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.entity.ProductEntity;
import com.capgemini.entity.TransactionEntity;
import com.capgemini.enums.Status;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionMapper {
    public static TransactionEntity toTransactionEntity(TransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            return null;
        }

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(transactionDTO.getId());
        transactionEntity.setTransactionDate(transactionDTO.getTransactionDate());
        transactionEntity.setStatus(transactionDTO.getStatus());
        transactionEntity.setPurchasesNumber(transactionDTO.getPurchasesNumber());
        transactionEntity.setVersion(transactionDTO.getVersion());


        return transactionEntity;

    }


    public static TransactionDTO toTransactioDTO(TransactionEntity transactionEntity) {
        if (transactionEntity == null) {
            return null;
        }

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transactionEntity.getId());
        transactionDTO.setTransactionDate(transactionEntity.getTransactionDate());
        transactionDTO.setStatus(transactionEntity.getStatus());
        transactionDTO.setPurchasesNumber(transactionEntity.getPurchasesNumber());
        transactionDTO.setVersion(transactionEntity.getVersion());


        if (transactionEntity.getProducts() != null) {
            transactionDTO.setProducts(transactionEntity.getProducts().stream().map(t->t.getId()).collect(Collectors.toList()));
        }
        return transactionDTO;
    }



}
