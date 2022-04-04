package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.api.rest.UserRestService;
import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Inject
    EntityManager em;

    public UserServiceImpl() {
    }

    public UserServiceImpl(EntityManager em) {
        this();
        this.em = em;
        this.em.persist(new User("john.doe", "john.doe@unige.ch", "0ce40162-aaaa-5666-aaaa-8f36f394ffd9"));
        this.em.persist(new User("user2", "user2@etu.unige.ch", "81391884-7b3e-4b80-a82b-d2445ba7e806"));
    }

    @Override
    public List<User> getAll() {
        var builder = em.getCriteriaBuilder();
        var criteria = builder.createQuery(User.class);
        return em.createQuery(criteria).getResultList();
    }

    @Override
    public User get(UUID id) {
        return em.find(User.class, id);
    }

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void delete(UUID id) {
        var toDelete = em.find(User.class, id);
        em.remove(toDelete);
    }
}
