package ch.unige.pinfo3.api.rest;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.service.JobService;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/jobs")
public class JobRestService {
    @Inject
    JobService jobService;

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public List<Job> getSearches(@QueryParam("page") int page) {
        return jobService.getAll();
    }
}
