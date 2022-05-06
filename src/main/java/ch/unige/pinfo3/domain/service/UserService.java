package ch.unige.pinfo3.domain.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.unige.pinfo3.domain.model.User;
import ch.unige.pinfo3.utils.QueryUtils;

/**
 * This class provides the interfaces for all actions involving users.
 */
@ApplicationScoped
public class UserService {
    // injects the persistence manager which connects to postgresql 
    // via jdbc and appropriate app.properties
    @Inject 
    EntityManager em;

    @Inject
    QueryUtils qu;

    @Transactional
    public List<User> getAll() {
        return qu.getAll(User.class, em);
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
        if(getAll().size() == 0) { // create John Doe user with fixed uuid.
            User cedric = new User("pendeville_ced", "bae@dieudonne", "c044a099-e489-43f8-9499-c04a371dbb61");
            em.persist(cedric);
            return cedric;
        } 
        user.uuid = UUID.randomUUID().toString();
        em.persist(user);
        return user;
    }

    @Transactional
    public User find(String uuid) {
        return em.find(User.class, uuid);
    }
}
