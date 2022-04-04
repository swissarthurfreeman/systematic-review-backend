package ch.unige.pinfo3.api.rest;

import ch.unige.pinfo3.domain.model.User;
import ch.unige.pinfo3.domain.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@ApplicationScoped
@Path("/users")
public class UserRestService {
//    @Inject
    UserService userService;

    public UserRestService() {
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User get(@PathParam("id") UUID id) {
        return userService.get(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User create(User user) {
        userService.create(user);
        return user;
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID id) {
        userService.delete(id);
    }
}
