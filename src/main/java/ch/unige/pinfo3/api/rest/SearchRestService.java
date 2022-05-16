package ch.unige.pinfo3.api.rest;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.domain.service.SearchService;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@Authenticated
@Path("/searches")
public class SearchRestService {
    @Inject
    JsonWebToken jwt;

    @Inject
    SearchService searchService;

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSearches() {
        List<Search> searches = searchService.getSearchesOf(jwt.getSubject());
        return Response.ok(searches).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response postUserSearch(@Valid Search search) {
        var err = searchService.syntaxAnalysis(search.query);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        search.user_uuid = jwt.getSubject();
        return Response.ok(searchService.create(search)).build();
    }

    @GET
    @Path("{search_uuid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSearch(@PathParam("search_uuid") String search_uuid) {
        var err = searchService.checkExistence(search_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).entity(err.get()).build();
        
        return Response.ok(searchService.getSearchOfUser(jwt.getSubject(), search_uuid)).build();
    }
}
