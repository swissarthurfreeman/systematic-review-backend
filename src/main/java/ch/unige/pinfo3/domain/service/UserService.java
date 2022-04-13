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

/**
 * This class provides the interfaces for all actions involving users.
 */
@ApplicationScoped
public class UserService {
    // injects the persistence manager which connects to postgresql 
    // via jdbc and appropriate app.properties
    @Inject 
    EntityManager em;

    @Transactional
    public List<User> getAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		criteria.from(User.class);
		return em.createQuery(criteria).getResultList();
    }

    /**
     * 
     * @param user a user body unmarshalled from json with a valid username and email.  
     * user.uuid is null when not provided in json body, if it is provided it is overwritten.
     * @return the created user
     * 
     * @ToDo request validation
     * <li>check username is valid</li>
     * <li>check email is valid</li>
     */
    @Transactional
    public User create(User user) {
        System.out.println(user.uuid);  
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
