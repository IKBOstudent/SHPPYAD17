package com.example.shppyad17.Departure;

import com.example.shppyad17.PostOffice.PostOffice;
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
public class DepartureService {
    private final SessionFactory sessionFactory;

    @Autowired
    public DepartureService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Departure> getAllDepartures() {
        Session session = sessionFactory.getCurrentSession();
        Query<Departure> query = session.createQuery("SELECT d FROM Departure d", Departure.class);
        return query.getResultList();
    }

    public List<Departure> getDeparturesByDate(String date) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Departure> query = cb.createQuery(Departure.class);
        Root<Departure> root = query.from(Departure.class);
        query.select(root).where(cb.equal(root.get("departureDate"), date));
        return session.createQuery(query).getResultList();
    }

    public List<Departure> getDeparturesByType(String type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Departure> query = cb.createQuery(Departure.class);
        Root<Departure> root = query.from(Departure.class);
        query.select(root).where(cb.equal(root.get("type"), type));
        return session.createQuery(query).getResultList();
    }

    public Departure getDepartureById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Departure.class, id);
    }



    public Departure addDeparture(Long officeId, Departure departure) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        PostOffice postOffice = session.get(PostOffice.class, officeId);
        if (postOffice == null) {
            return null;
        }
        postOffice.addDeparture(departure);
        session.persist(postOffice);
        departure.setPostOffice(postOffice);
        session.persist(departure);
        tx.commit();
        return departure;
    }

    public void deleteDepartureById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Departure departure = session.get(Departure.class, id);
        if (departure != null) {
            session.remove(departure);
        }
        tx.commit();
    }

}