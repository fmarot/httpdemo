package com.teamtter.httpdemo.server;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ServerApp {

	public static void main(String[] args) {
		SpringApplication.run(ServerApp.class, args);
		log.info("Server Main done");
	}
	
//	@Bean
//	public SessionFactory getSessionFactory(EntityManagerFactory entityManagerFactory) {
//	    if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
//	        throw new NullPointerException("factory is not a hibernate factory");
//	    }
//	    return entityManagerFactory.unwrap(SessionFactory.class);
//	}
//	@Bean // Need to expose SessionFactory to be able to work with BLOBs
//	public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
//		return hemf.getSessionFactory();
//	}

}