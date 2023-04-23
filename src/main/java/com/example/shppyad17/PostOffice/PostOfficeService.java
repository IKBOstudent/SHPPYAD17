package com.example.shppyad17.PostOffice;

import com.example.shppyad17.Departure.Departure;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostOfficeService {
    private final SessionFactory sessionFactory;

    @Autowired
    public PostOfficeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<PostOffice> getAllPostOffices() {
        Session session = sessionFactory.getCurrentSession();
        Query<PostOffice> query = session.createQuery("SELECT d FROM PostOffice d", PostOffice.class);
        return query.getResultList();
    }

    public List<PostOffice> getPostOfficesByCity(String city) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PostOffice> query = cb.createQuery(PostOffice.class);
        Root<PostOffice> root = query.from(PostOffice.class);
        query.select(root).where(cb.equal(root.get("cityName"), city));
        return session.createQuery(query).getResultList();
    }

    public List<PostOffice> getPostOfficesByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PostOffice> query = cb.createQuery(PostOffice.class);
        Root<PostOffice> root = query.from(PostOffice.class);
        query.select(root).where(cb.equal(root.get("name"), name));
        return session.createQuery(query).getResultList();
    }

    public PostOffice getPostOfficeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(PostOffice.class, id);
    }

    public PostOffice createPostOffice(PostOffice postOffice) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(postOffice);
        tx.commit();
        return postOffice;
    }

    public void deletePostOfficeById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        PostOffice postOffice = session.get(PostOffice.class, id);
        if (postOffice != null) {
            session.remove(postOffice);
        }
        tx.commit();
    }

    public List<Departure> getDeparturesByPostOffice(Long id) {
        Session session = sessionFactory.openSession();
        PostOffice postOffice = session.get(PostOffice.class, id);
        return postOffice.getDepartureList();
    }
}
