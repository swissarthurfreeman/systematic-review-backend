package ch.unige.pinfo3.api.rest;

import ch.unige.pinfo3.domain.service.JobService;
import ch.unige.pinfo3.utils.ErrorReport;
import ch.unige.pinfo3.utils.VALID_UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;

@ApplicationScoped
@Authenticated
@Path("/jobs")
public class JobRestService {
    @Inject
    JobService jobService;

    @Inject
    JsonWebToken jwt;

    @GET // /jobs/:id
    @Path("{job_uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJob(@PathParam("job_uuid") @VALID_UUID String job_uuid) {
        Log.info("getting job");
        
        var job = jobService.getJob(job_uuid);
        Log.info(job.isPresent());
        if(!job.isPresent()) {
            Log.info("job is not present");
            var err = new ErrorReport();
            err.errors
                    .add(
                        new ErrorReport.Error(
                            "A Job with job_uuid does not exist",
                            "Check the results", 
                            Response.Status.NOT_FOUND
                        )
                    );
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        } else {
            return Response.ok(job.get()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserJobs() {
        return Response.ok(jobService.getJobsOfUser(jwt.getSubject())).build();
    }
}
