package com.capgemini.mapper;

import com.capgemini.dto.TransactionDTO;
import com.capgemini.entity.TransactionEntity;

import java.util.Iterator;
import java.util.LinkedList;
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
            transactionDTO.setProducts(transactionEntity.getProducts().stream().map(t -> t.getId()).collect(Collectors.toList()));
        }
        return transactionDTO;
    }


    public static List<TransactionDTO> toTransactionTOList(Iterable<TransactionEntity> transactions) {
        Iterator<TransactionEntity> it = transactions.iterator();
        List<TransactionDTO> transactionsTO = new LinkedList<>();

        while (it.hasNext()) {
            transactionsTO.add(toTransactioDTO(it.next()));
        }

        return transactionsTO;
    }


}
