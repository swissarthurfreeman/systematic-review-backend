package ch.unige.pinfo3;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

@Path("/jobs")
public class JobResource {
    private Set<Job> jobs = Collections.synchronizedSet(new LinkedHashSet<>());

    JobResource() {
        User John = new User("John_Doe", "john@doe.com");
        jobs.add(new Job("HIV AND SAHARA", John));
        jobs.add(new Job("BRAZIL OR ZIKA", John));
    }

    @POST
    public create(Job job) {
        jobs.add(job);
    }
}
