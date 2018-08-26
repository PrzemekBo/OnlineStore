package com.capgemini.dao.impl;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dao.custom.CustomerDaoCustom;
import com.capgemini.entity.CustomerEntity;
import com.capgemini.entity.QCustomerEntity;
import com.capgemini.entity.QProductEntity;
import com.capgemini.entity.QTransactionEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDaoImpl implements CustomerDaoCustom {


    @PersistenceContext
    EntityManager entityManager;


    JPAQueryFactory queryFactory;
    QProductEntity product;
    QTransactionEntity transaction;
    QCustomerEntity customer;


    @PostConstruct
    private void setup() {
        queryFactory = new JPAQueryFactory(this.entityManager);
        transaction = QTransactionEntity.transactionEntity;
        product = QProductEntity.productEntity;
        customer= QCustomerEntity.customerEntity;
    }


    @Override
    public List<CustomerEntity> findThreeBestClientsInSomeTimePeriod(Date startDate, Date endDate) {

        NumberPath<Long> sum = Expressions.numberPath(Long.class, "s");


        List<Tuple> tuples = queryFactory.selectFrom(customer)
                .select(customer, product.price.sum().as(sum))
                .innerJoin(customer.transactions,transaction)
                .innerJoin(transaction.products, product)
                .groupBy(customer.id)
                .where(transaction.transactionDate.between(startDate, endDate))
                .orderBy(sum.desc())
                .limit(3)
                .fetch();

        return tuples.stream().map(t -> (CustomerEntity) t.toArray()[0]).collect(Collectors.toList());

    }
}
