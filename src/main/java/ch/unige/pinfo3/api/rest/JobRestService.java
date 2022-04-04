package ch.unige.pinfo3.api.rest;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;
import ch.unige.pinfo3.domain.service.JobService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("/jobs")
public class JobRestService {
    @Inject
    JobService jobService;
    private final User testUser;

    public JobRestService() {
        testUser = jobService.getDummyUser();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Job create(String query) {
        Job newJob = new Job(query, testUser, UUID.randomUUID().toString());
        jobService.create(newJob);
        return newJob;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Job> getJobs() {
        return jobService.getAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Job get(@PathParam("id") UUID id) {
        return jobService.get(id);
    }
}
