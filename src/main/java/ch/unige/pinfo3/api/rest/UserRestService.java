package ch.unige.pinfo3.api.rest;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.domain.model.User;
import ch.unige.pinfo3.domain.service.SearchService;
import ch.unige.pinfo3.domain.service.UserService;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;;

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

    @Inject
    Logger LOG;

    public static final EmailValidator ev = EmailValidator.getInstance();

    @GET // /users
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(@QueryParam("page") int page) {
        return userService.getAll();
    }
    
    @GET // /users/:id
    @Path("{uuid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("uuid") String uuid) {
        return userService.find(uuid);
    }

    @PUT // /users/:id
    @Path("{uuid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response putUser(@PathParam("uuid") String uuid, User user) {
        user.uuid = uuid;
        User updated_user;
        try {
            updated_user = userService.update(user);
        } catch(Exception e) {
            var err = new Error(
                "Username or email already exists",
                "Try a different username or email", 
                Response.Status.CONFLICT
            );
            return Response.status(Response.Status.CONFLICT).entity(err).build();   
        }
        return Response.ok(updated_user).build();
    }

    @POST // /users
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(User user) {
        Response error = validateUser(user);
        if(error != null)
            return error;

        User created_user;
        try {
            created_user = userService.create(user);  
        } catch(Exception e) {
            LOG.error(e.getMessage());
            var err = new Error(
                "Username or email already exists",
                "Try a different username or email", 
                Response.Status.CONFLICT
            );
            return Response.status(Response.Status.CONFLICT).entity(err).build();
        }
        return Response.ok(created_user).build();
    }

    @GET
    @Path("{uuid}/searches")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<Search> getUserSearch(@PathParam("uuid") String user_uuid) {
        return searchService.getSearchesOf(user_uuid);
    }

    @POST
    @Path("{uuid}/searches")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Search postUserSearch(@PathParam("uuid") String user_uuid, Search search) {
        search.user_uuid = user_uuid;
        return searchService.create(search);
    }

    private static Response validateUser(User user) {
        if(!ev.isValid(user.email))
            return Response.status(Response.Status.CONFLICT).entity(
                new Error(
                    "Invalid email format",
                    "email must be a valid email format with ascii characters and @ symbol", 
                    Response.Status.BAD_REQUEST)
                ).build();

        if(!GenericValidator.matchRegexp(user.username, "^(?=.{5,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")) 
            return Response.status(Response.Status.BAD_REQUEST).entity(
                new Error(
                    "Invalid username format", 
                    "username must be ascii, at least 5 characters long and at most 20, must start by ascii character",
                    Response.Status.BAD_REQUEST)
                ).build();
        return null;
    }
}