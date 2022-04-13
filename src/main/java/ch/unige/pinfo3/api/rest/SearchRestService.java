package ch.unige.pinfo3.api.rest;

import java.util.List;
import java.util.UUID;

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

@ApplicationScoped
@Path("/searches")
public class SearchRestService {
    @Inject
    SearchService searchService;
    
    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<Search> getSearches(@QueryParam("page") int page) {
        return searchService.getAll();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Search create(Search search) {
        return searchService.create(search);
    }
}
