package ch.unige.pinfo3.api.rest;

import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.service.ArticleService;
import ch.unige.pinfo3.domain.service.ResultService;
import ch.unige.pinfo3.utils.ErrorReport;
import ch.unige.pinfo3.utils.VALID_UUID;
import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;

import java.util.List;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Authenticated
@Path("/results")
public class ResultRestService {
    @Inject
    ResultService resultService;

    @Inject
    ArticleService ass;


    @GET // /results
    @Produces(MediaType.APPLICATION_JSON)
    public List<Result> getResults(@QueryParam("page") int page) {
        return resultService.getAll();
    }
    
    @GET // /results/:id
    @Path("{result_uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResult(@PathParam("result_uuid") @VALID_UUID String result_uuid) {
        var res = resultService.getResult(result_uuid);
        if(res.isEmpty()) {
            var err = new ErrorReport();
            err.errors
                .add(
                    new ErrorReport.Error(
                        "A Result with result_uuid does not exist", 
                        "Please provide a valid result_uuid", 
                        Response.Status.NOT_FOUND)
                );
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        } else {
            return Response.ok(res.get()).build();
        }
    }

    @GET // /results/:id/articles
    @Path("{result_uuid}/articles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResultArticles(@PathParam("result_uuid") @VALID_UUID String result_uuid) {
        var err = resultService.checkExistence(result_uuid);
        if(err.isPresent())
            return Response.status(Response.Status.NOT_FOUND).entity(err.get()).build();

        return Response.ok(ass.getArticlesOf(result_uuid)).build();
    }
}
