package com.capgemini.criteria;

import lombok.Data;

import java.util.Date;


@Data
public class TransactionSearchCriteria {

    private String email;
    private Date dateFrom;
    private Date dateTo;
    private Long productId;
    private Long trnsactionPrice;

/*    private String name = "";
    private Date dateFrom = null;
    private Date dateTo = null;
    private String productName = "";
    private Double totalTransactionAmountFrom = null;
    private Double totalTransactionAmountTo = null;*/
}
