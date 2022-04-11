package ch.unige.pinfo3.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ch.unige.pinfo3.domain.model.User;

@ApplicationScoped
@Path("/users")
public class UserRestService {

    @Inject // injects the persistence manager which connects to postgresql.
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(@QueryParam("page") int page) {
        System.out.println(page);
        ArrayList<User> list = new ArrayList<>();
        list.add(new User("john", "doe@mail.com"));
        list.add(new User("jonass", "jonass@mail.com"));
        return list;
    }
    
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public User create(User user) {
        //em.persist(user);
        return user;
        //em.persist(user);
        //return user;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") String id) {
        UUID user_id = UUID.fromString(id);
        return em.find(User.class, user_id);
    }
}
