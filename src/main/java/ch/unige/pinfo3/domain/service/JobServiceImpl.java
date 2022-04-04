package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

public class JobServiceImpl implements JobService {
    @Inject
    EntityManager em;

    public JobServiceImpl() {
    }

    public JobServiceImpl(EntityManager em) {
        this();
        this.em = em;
        var john = em.find(User.class, UUID.fromString("0ce40162-aaaa-5666-aaaa-8f36f394ffd9"));
        em.persist(new Job("HIV AND SAHARA", john, "0ce40162-76a7-4863-8a1b-8f36f394ffd9"));
        em.persist(new Job("BRAZIL OR ZIKA", john, "b6bb6a2a-5cef-4990-98b2-52a359fbaa77"));
    }

    @Override
    public List<Job> getAll() {
        var builder = em.getCriteriaBuilder();
        var criteria = builder.createQuery(Job.class);
        return em.createQuery(criteria).getResultList();
    }

    @Override
    public Job get(UUID id) {
        return em.find(Job.class, id);
    }

    @Override
    public User getDummyUser() {
        return em.find(User.class, UUID.fromString("0ce40162-aaaa-5666-aaaa-8f36f394ffd9"));
    }

    @Override
    public void create(Job job) {
        em.persist(job);
    }
}
