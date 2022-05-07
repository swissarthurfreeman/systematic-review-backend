/**
 * This class takes care of all /users/* endpoints
 * E.g.
 * /users GET, POST
 * /users/:id GET, PUT
 * /users/:id/jobs GET
 * /users/:id/searches POST, GET
 * /users/:id/searches/:id GET
 * /users/:id/jobs GET
 * 
 */

package ch.unige.pinfo3.api.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
    public Response getUser(@PathParam("user_uuid") String user_uuid) {
        var maybe_an_error = validateUUID(user_uuid);
        if(maybe_an_error.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(maybe_an_error.get()).build();
        User usr = userService.find(user_uuid);

        if(usr == null)
            return Response.status(Response.Status.NOT_FOUND).entity(
                new Error(
                    "invalid user_uuid",
                    "a user with that uuid does not exist",
                    Response.Status.NOT_FOUND
                )
            ).build();
        return Response.ok(usr).build();
    }

    @PUT // /users/:id
    @Path("{user_uuid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response putUser(@PathParam("user_uuid") String user_uuid, User user) {
        var maybe_an_error = validateUUID(user_uuid);
        if(maybe_an_error.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(maybe_an_error.get()).build();
        
        maybe_an_error = userService.validateUser(user_uuid);
        if(maybe_an_error.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(maybe_an_error.get()).build();

        user.uuid = user_uuid;
        User updated_user;
        try {
            updated_user = userService.update(user);
        } catch(Exception e) {
            var err = new Error(
                "Username or email being updated to already exists",
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
        var error = userService.validateEmail(user.email);
        if(error.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(error.get()).build();

        error = userService.validateUsername(user.username);
        if(error.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(error.get()).build();

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
    public Response getUserSearches(@PathParam("uuid") String user_uuid) {
        var err = validateUUID(user_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        err = userService.validateUser(user_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        List<Search> searches = searchService.getSearchesOf(user_uuid);
        return Response.ok(searches).build();
    }

    @POST
    @Path("{uuid}/searches")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response postUserSearch(@PathParam("uuid") String user_uuid, Search search) {
        var err = validateUUID(user_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        err = userService.validateUser(user_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        err = searchService.syntaxAnalysis(search.query);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        search.user_uuid = user_uuid;
        return Response.ok(searchService.create(search)).build();
    }

    @GET
    @Path("{user_uuid}/searches/{search_uuid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSearch(@PathParam("user_uuid") String user_uuid, @PathParam("search_uuid") String search_uuid) {
        var err = validateUUID(user_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        err = userService.validateUser(user_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        err = searchService.validateSearch(search_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        return Response.ok(searchService.getSearchOfUser(user_uuid, search_uuid)).build();
    }

    /**
     * Helper function to check user validity. Username and email must be 
     * correctly formatted, if so returns null, if not returns a response
     * with the appropriate Error configured. 
     * @param user a User object. 
     * @return an optional containing or not a Response. 
     */
    private static Optional<Error> validateUUID(String uuid) {
        try {
            UUID.fromString(uuid);     // make sure it's a valid uuid
        } catch(IllegalArgumentException e) {
            var err = new Error(
                "invalid uuid",
                "The uuid provided is syntaxically incorrect", 
                Response.Status.BAD_REQUEST
            );
            return Optional.of(err);
        }
        return Optional.empty();
    }
}