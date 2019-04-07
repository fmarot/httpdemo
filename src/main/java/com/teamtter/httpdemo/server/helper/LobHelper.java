package com.teamtter.httpdemo.server.helper;

import java.io.InputStream;
import java.sql.Blob;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
public class LobHelper {

//    private final SessionFactory sessionFactory;
    
    @PersistenceContext
private EntityManager entityManager;

//    @Autowired
//    public LobHelper(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }

    public Blob createBlob(InputStream content, long size) {
    	 Session session = entityManager.unwrap(Session.class);
    	return Hibernate.getLobCreator(session).createBlob(content, size);
//        return sessionFactory.getCurrentSession().getLobHelper().createBlob(content, size);
    }
}
