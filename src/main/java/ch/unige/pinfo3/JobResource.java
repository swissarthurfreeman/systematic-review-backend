package ch.unige.pinfo3;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Path("/jobs")
public class JobResource {
    private Set<Job> jobs = Collections.synchronizedSet(new LinkedHashSet<>());

    JobResource() {
        User John = new User()
        jobs.add(new Job("HIV AND SAHARA", John));
        jobs.add(new Job("BRAZIL OR ZIKA", John));
    }

    @POST
    public create(Job job) {
        jobs.add(job);
    }
}
