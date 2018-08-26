package com.capgemini.dao.impl;

import com.capgemini.dao.custom.TransactionDaoCustom;
import com.capgemini.entity.QProductEntity;
import com.capgemini.entity.QTransactionEntity;
import com.capgemini.enums.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

public class TransactionDaoImpl implements TransactionDaoCustom {


    @PersistenceContext
    EntityManager entityManager;


    JPAQueryFactory queryFactory;
    QProductEntity product;
    QTransactionEntity transaction;


    @PostConstruct
    private void setup() {
        queryFactory = new JPAQueryFactory(this.entityManager);
        transaction = QTransactionEntity.transactionEntity;
        product = QProductEntity.productEntity;
    }



    @Override
    public Long calculateProfitInSomeTimePeriod(Date startDate, Date endDate) {
        return queryFactory.selectFrom(transaction)
                .select(product.price.multiply(product.margin)
                        .sum())
                .innerJoin(transaction.products, product)
                .where(transaction.transactionDate
                        .between(startDate, endDate))
                .fetchOne();
    }

    @Override
    public Long sumAllPriceOfTransactionsForCustomer(Long id) {
        return queryFactory.selectFrom(transaction)
                .select(product.price.sum())
                .innerJoin(transaction.products, product)
                .where(transaction.customer.id.eq(id))
                .fetchOne();
    }

    @Override
    public Long calculateAllPriceOfTransactionsForCustomerByStatus(Long id, Status status) {
        return queryFactory.selectFrom(transaction)
                .select(product.price.sum())
                .innerJoin(transaction.products, product)
                .where(transaction.customer.id.eq(id).and(transaction.status.eq(status)))
                .fetchOne();
    }

}
