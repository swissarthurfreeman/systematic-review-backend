package ch.unige.pinfo3;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;
import java.util.ArrayList;

@Path("/jobs")
public class JobResource {
    private ArrayList<Job> jobs = new ArrayList<>();

    public JobResource() {
        // hard-coded uuids to test.
        jobs.add(new Job("HIV AND SAHARA", john, "0ce40162-76a7-4863-8a1b-8f36f394ffd9"));
        jobs.add(new Job("BRAZIL OR ZIKA", john, "b6bb6a2a-5cef-4990-98b2-52a359fbaa77"));
    }

    // bogus john doe user.
    public static User john = new User("John_Doe", "john@doe.com");

    @POST
    public ArrayList<Job> add(Job job) {
        jobs.add(job);
        return jobs;
    }

    @GET public ArrayList<Job> getJobs() {
        return jobs;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Job get(@PathParam("id") String id) {

        for (Job curr : jobs)
            if (curr.getId().toString().equals(id))
                return curr;

        // if no job with this id is found return null.
        return null;
    }
}
