package com.clone.ohouse;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class QuerydslConfig {
    @Bean
    public JPAQueryFactory setJPAQueryFactory(EntityManager em){
        return new JPAQueryFactory(em);
    }
}