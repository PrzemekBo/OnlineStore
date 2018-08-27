package com.capgemini.dao.impl;

import com.capgemini.criteria.TransactionSearchCriteria;
import com.capgemini.dao.custom.TransactionDaoCustom;
import com.capgemini.entity.QProductEntity;
import com.capgemini.entity.QTransactionEntity;
import com.capgemini.entity.TransactionEntity;
import com.capgemini.enums.Status;
import com.capgemini.exception.TransactionSearchCriteriaException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

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


    /**
     * Calculate the profit from a defined time period
     * @param startDate The beginning of the period from which we are looking for profits
     * @param endDate  The ending of the period from which we are looking for profits
     * @return profits
     */
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

    /**
     * Calculate the value of all customer transactions by cusomer id
     * @param id id of customer
     * @return value of transactions
     */
    @Override
    public Long sumAllPriceOfTransactionsForCustomer(Long id) {
        return queryFactory.selectFrom(transaction)
                .select(product.price.sum())
                .innerJoin(transaction.products, product)
                .where(transaction.customer.id.eq(id))
                .fetchOne();
    }

    /**
     * Calculate the value of all customer transactions by cusomer id and status
     * @param id id of customer
     * @param status status of transaction
     * @return value of transactions
     */
    @Override
    public Long calculateAllPriceOfTransactionsForCustomerByStatus(Long id, Status status) {
        return queryFactory.selectFrom(transaction)
                .select(product.price.sum())
                .innerJoin(transaction.products, product)
                .where(transaction.customer.id.eq(id).and(transaction.status.eq(status)))
                .fetchOne();
    }

    /**
     * Calculate the value of all customers transactions by and status
     * @param status customers status
     * @return  value of transactions
     */
    @Override
    public Long calculateAllPriceOfTransactionsForAllCustomersByStatus(Status status) {
        return queryFactory.selectFrom(transaction)
                .select(product.price.sum())
                .innerJoin(transaction.products, product)
                .where(transaction.status.eq(status))
                .fetchOne();
    }

    /**
     * Searches transactions by 4 criteria.
     * @param transactionSearchCriteria customer email, start date, ending date, id of product, transaction price
     * @return transaction
     * @throws TransactionSearchCriteriaException if no criterion is defined
     */
    @Override
    public List<TransactionEntity> searchTransactionByFourCriteria(TransactionSearchCriteria transactionSearchCriteria)throws TransactionSearchCriteriaException  {
        BooleanBuilder query = new BooleanBuilder();

        if (transactionSearchCriteria.getEmail() == null
                && transactionSearchCriteria.getDateFrom() == null && transactionSearchCriteria.getDateTo() == null
                && transactionSearchCriteria.getProductId() == null
                && transactionSearchCriteria.getTrnsactionPrice() == null) {
            throw new TransactionSearchCriteriaException("You must provide at least one criterion");
        }

        if (transactionSearchCriteria.getEmail() != null) {
            query.and(transaction.customer.email.eq(transactionSearchCriteria.getEmail()));
        }

        if (transactionSearchCriteria.getDateFrom() != null && transactionSearchCriteria.getDateTo() != null) {
            query.and(transaction.transactionDate.between(transactionSearchCriteria.getDateFrom(), transactionSearchCriteria.getDateTo()));
        }

        if (transactionSearchCriteria.getProductId() != null) {
            query.and(product.id.eq(transactionSearchCriteria.getProductId()));
        }

        if (transactionSearchCriteria.getTrnsactionPrice() == null) {
            return queryFactory.selectFrom(transaction)
                    .innerJoin(transaction.products, product)
                    .where(query)
                    .groupBy(transaction.id)
                    .fetch();
        } else {
            return queryFactory.selectFrom(transaction)
                    .innerJoin(transaction.products, product)
                    .where(transaction.id.in(
                            JPAExpressions.select(transaction.id)
                                    .from(product)
                                    .innerJoin(product.transactions, transaction)
                                    .groupBy(transaction.id)
                                    .having(product.price.sum().eq(transactionSearchCriteria.getTrnsactionPrice()))
                    )
                            .and(query))
                    .groupBy(transaction.id).fetch();
        }
    }
}
