package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class JobServiceImpl implements JobService {
    private final List<Job> jobs;
    private final User john;

    public JobServiceImpl() {
        jobs = new ArrayList<>();
        john = User.getDummy();
        jobs.add(new Job("HIV AND SAHARA", john));
        jobs.add(new Job("HIV AND SAHARA", john));
    }

    @Override
    public List<Job> getAll() {
        return jobs;
    }

    @Override
    public Job get(UUID id) {
        return jobs.stream().filter(job -> id.equals(job.getId())).findAny().orElse(null);
    }

    @Override
    public void create(Job job) {
        jobs.add(job);
    }
}
