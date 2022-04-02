package ch.unige.pinfo3;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.UUID;

@Path("/jobs")
public class JobResource {
    private final ArrayList<Job> jobs = new ArrayList<>();

    // bogus john doe user is first inserted in database.
    public static User john = new User("john.doe", "john.doe@unige.ch", "0ce40162-aaaa-5666-aaaa-8f36f394ffd9");

    public JobResource() {
        // hard-coded uuids to test.
        jobs.add(new Job("HIV AND SAHARA", john, "0ce40162-76a7-4863-8a1b-8f36f394ffd9"));
        jobs.add(new Job("BRAZIL OR ZIKA", john, "b6bb6a2a-5cef-4990-98b2-52a359fbaa77"));
    }

    @POST
    public Job add(String query) {
        Job newJob = new Job(query, john, UUID.randomUUID().toString() );
        jobs.add(newJob);
        return newJob;
    }

    @GET
    public ArrayList<Job> getJobs() {
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
