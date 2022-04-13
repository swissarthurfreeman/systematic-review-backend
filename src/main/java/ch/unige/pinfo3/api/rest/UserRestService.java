package ch.unige.pinfo3.api.rest;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.domain.model.User;
import ch.unige.pinfo3.domain.service.SearchService;
import ch.unige.pinfo3.domain.service.UserService;

/**
 * This class calls appropriate services for all routes 
 * to /users and /users/*.
 */
@ApplicationScoped
@Path("/users")
public class UserRestService {
    @Inject
    UserService userService;

    @Inject
    SearchService searchService;

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(@QueryParam("page") int page) {
        return userService.getAll();
    }
    
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public User create(User user) {
        return userService.create(user); 
    }

    @GET
    @Path("{uuid}/searches")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<Search> getUserSearch(@PathParam("uuid") String user_uuid) {
        return searchService.getSearchesOf(user_uuid);
    }

    @GET
    @Path("{uuid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("uuid") String uuid) {
        return userService.find(uuid);
    }
}