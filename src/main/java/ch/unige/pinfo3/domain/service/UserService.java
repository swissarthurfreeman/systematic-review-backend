package ch.unige.pinfo3.domain.service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

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

    @Inject
    Logger LOG;

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
    public User create(User user) throws Exception {
        if(user.uuid == null)
            user.uuid = UUID.randomUUID().toString();
        try {
            em.persist(user);
            em.flush();
        } catch(Exception e) {
            throw e;
        }
        return user;
    }

    @Transactional
    public User update(User user) throws Exception {
        try {
            em.merge(user);
            em.flush();
        } catch(Exception e) {
            throw e;
        }
        return user;
    }

    @Transactional
    public User find(String uuid) {
        return em.find(User.class, uuid);
    }
}
