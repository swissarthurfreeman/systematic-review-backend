package ch.unige.pinfo3.domain.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import ch.unige.pinfo3.domain.model.User;

@ApplicationScoped
public class UserService {
    @Inject // injects the persistence manager which connects to postgresql.
    EntityManager em;

    @Transactional
    public List<User> getAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		criteria.from(User.class);
		return em.createQuery(criteria).getResultList();
    }

    @Transactional
    public User create(User user) {
        user.uuid = UUID.randomUUID().toString();
        em.persist(user);
        return user;
    }

    @Transactional
    public User find(String uuid) {
        System.out.println(uuid);
        return em.find(User.class, uuid);
    }
}
