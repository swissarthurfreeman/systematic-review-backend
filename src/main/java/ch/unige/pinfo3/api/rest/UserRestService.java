/**
 * This class takes care of all /users/* endpoints
 * E.g.
 * /users GET, POST
 * /users/:id GET, PUT
 * /users/:id/jobs GET
 * /users/:id/searches POST, GET
 * /users/:id/searches/:id GET
 * /users/:id/jobs GET
 * TODO comment interface thoroughly
 */

package ch.unige.pinfo3.api.rest;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
import ch.unige.pinfo3.domain.service.JobService;
import ch.unige.pinfo3.domain.service.SearchService;
import ch.unige.pinfo3.domain.service.UserService;
import ch.unige.pinfo3.utils.ErrorReport;
import ch.unige.pinfo3.utils.VALID_UUID;


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
    JobService jobService;

    @Inject
    Logger logger;

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
    public Response getUser(@PathParam("user_uuid") @VALID_UUID String user_uuid) {
        User usr = userService.find(user_uuid);
        if(usr == null) {
            var err = new ErrorReport();
            err.errors.add(
                new ErrorReport.Error(
                    "User with that uuid does not exist",
                "Try a different uuid", 
                    Response.Status.NOT_FOUND
                )
            );
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        }
        return Response.ok(usr).build();
    }

    @PUT // /users/:id
    @Path("{user_uuid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response putUser(@PathParam("user_uuid") @VALID_UUID String user_uuid, @Valid User user) {
        user.uuid = user_uuid;
        User updated_user;
        try {
            updated_user = userService.update(user);
        } catch(Exception e) {
            var err = new ErrorReport.Error(
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
    public Response create(@Valid User user) {
        User created_user;
        try {
            created_user = userService.create(user);
        } catch(Exception e) {
            logger.error(e.getMessage());
            var err = new ErrorReport();
            err.errors.add(
                new ErrorReport.Error(
                    "Username or email already exists",
                  "Try a different username or email", 
                    Response.Status.CONFLICT
                )
            );
            return Response.status(Response.Status.CONFLICT).entity(err).build();
        }
        return Response.ok(created_user).build();
    }

    @GET
    @Path("{uuid}/searches")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSearches(@PathParam("uuid") @VALID_UUID String user_uuid) {
        var err = userService.checkExistence(user_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        List<Search> searches = searchService.getSearchesOf(user_uuid);
        return Response.ok(searches).build();
    }

    @POST
    @Path("{uuid}/searches")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response postUserSearch(@PathParam("uuid") @VALID_UUID String user_uuid, @Valid Search search) {
        
        var err = userService.checkExistence(user_uuid);
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
        
        var err = searchService.checkExistence(search_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        return Response.ok(searchService.getSearchOfUser(user_uuid, search_uuid)).build();
    }

    @GET
    @Path("{user_uuid}/jobs")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserJobs(@PathParam("user_uuid") String user_uuid) {
        return Response.ok(jobService.getJobsOfUser(user_uuid)).build();
    }
}