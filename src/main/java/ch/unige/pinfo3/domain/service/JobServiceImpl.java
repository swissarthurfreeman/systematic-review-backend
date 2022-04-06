package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class JobServiceImpl implements JobService {
    private final List<Job> jobs;
    private final User john;

    public JobServiceImpl() {
        jobs = new ArrayList<>();
        john = new User("john.doe", "john.doe@unige.ch", "0ce40162-aaaa-5666-aaaa-8f36f394ffd9");
        jobs.add(new Job("HIV AND SAHARA", john, "0ce40162-76a7-4863-8a1b-8f36f394ffd9"));
        jobs.add(new Job("HIV AND SAHARA", john, "0ce40162-76a7-4863-8a1b-8f36f394ffd9"));
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
    public User getDummyUser() {
        return john;
    }

    @Override
    public void create(Job job) {
        jobs.add(job);
    }
}
