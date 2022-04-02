package ch.unige.pinfo3;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

@Path("/jobs")
public class JobResource {
    private Set<Job> jobs = Collections.newSetFromMap(
            Collections.synchronizedMap(new LinkedHashMap<>()));

    public JobResource() {
        jobs.add(new Job("HIV AND SAHARA", john));
        jobs.add(new Job("BRAZIL OR ZIKA", john));
    }

    // bogus john doe user.
    public static User john = new User("John_Doe", "john@doe.com");


    @POST
    public Set<Job> add(Job job) {
        jobs.add(job);
        return jobs;
    }

    @GET public Set<Job> getJobs() {
        return jobs;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Job get(@PathParam("id") String id) {

        for (Job curr : jobs) {
            return curr;            // returns random first element for now.
        }
        return new Job("LOL AND MDR", john);
    }
}
