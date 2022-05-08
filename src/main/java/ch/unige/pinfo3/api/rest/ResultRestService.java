package ch.unige.pinfo3.api.rest;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.service.ArticleService;
import ch.unige.pinfo3.domain.service.ResultService;
import ch.unige.pinfo3.utils.ErrorReport;
import ch.unige.pinfo3.utils.VALID_UUID;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/results")
public class ResultRestService {
    @Inject
    ResultService resultService;

    @Inject
    ArticleService ass;

    @GET // /results
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<Result> getResults(@QueryParam("page") int page) {
        return resultService.getAll();
    }

    @GET // /results/:id
    @Path("{uuid}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResult(@PathParam("uuid") @VALID_UUID String result_uuid) {
        var res = resultService.getResult(result_uuid);
        if(!res.isPresent())
            Response.status(Response.Status.NOT_FOUND)
            .entity(
                new ErrorReport().errors.add(
                    new ErrorReport.Error("A Result with result_uuid does not exist", "Please provide a valid result_uuid", Response.Status.NOT_FOUND)
                ));
        return Response.ok(res.get()).build();
    }

    @GET // /results/:id/articles
    @Path("{uuid}/articles")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> getResultArticles(@PathParam("uuid") @VALID_UUID String result_uuid) {
        return ass.getArticlesOf(result_uuid);
    }
}
