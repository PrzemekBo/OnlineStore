package com.capgemini.dao.impl;

import com.capgemini.dao.custom.ProductDaoCustom;
import com.capgemini.entity.ProductEntity;
import com.capgemini.entity.QProductEntity;
import com.capgemini.entity.QTransactionEntity;
import com.capgemini.enums.Status;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.Tuple;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoImpl implements ProductDaoCustom {

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
    public List<ProductEntity> findTenBestSellers() {
        NumberPath<Long> result = Expressions.numberPath(Long.class, "c");
        List<Tuple> tuples = queryFactory.selectFrom(product)
                .select(product, product.id.count().as(result))
                .innerJoin(product.transactions, transaction)
                .groupBy(product.id)
                .orderBy(result.desc())
                .limit(10L)
                .fetch();

        List<ProductEntity> products = tuples.stream().map(t -> (ProductEntity) t.toArray()[0]).collect(Collectors.toList());

        return products;
    }

    @Override
    public List<Tuple> findItemsInInDeliveryStatus() {
        NumberPath<Long> count = Expressions.numberPath(Long.class, "c");
        return queryFactory.selectFrom(transaction)
                .select(product.productName, product.id.count().as(count))
                .join(transaction.products, product)
                .where(transaction.status.eq(Status.IN_DELIVERY))
                .groupBy(product.id)
                .fetch();
    }

}
