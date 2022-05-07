package ch.unige.pinfo3.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.GenericValidator;

import org.jboss.logging.Logger;

import ch.unige.pinfo3.domain.model.User;
import ch.unige.pinfo3.utils.QueryUtils;
import ch.unige.pinfo3.api.rest.Error;


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

    public static final EmailValidator ev = EmailValidator.getInstance();

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

    @Transactional
    public Optional<Error> validateUser(String user_uuid) {
        if(find(user_uuid) == null) {
            var err = new Error(
                "invalid user_uuid",
                "The uuid provided does not refer to an existing user", 
                Response.Status.BAD_REQUEST
            );
            return Optional.of(err);
        }
        return Optional.empty();
    }

    public Optional<Error> validateEmail(String email) {
        if(!ev.isValid(email))
            return Optional.of(
                new Error(
                    "Invalid email format",
                    "email must be a valid email format with ascii characters and @ symbol", 
                    Response.Status.BAD_REQUEST
                )
            );
        return Optional.empty();
    }

    public Optional<Error> validateUsername(String username) {
        if(!GenericValidator.matchRegexp(username, "^(?=.{5,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")) 
            return Optional.of(
                new Error(
                    "Invalid username format", 
                    "username must be ascii, at least 5 characters long and at most 20, must start by ascii character",
                    Response.Status.BAD_REQUEST
                )
            );
        return Optional.empty();
    }
}
