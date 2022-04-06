package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface JobService {
    List<Job> getAll();
    Job get(UUID id);
    User getDummyUser();
    void create(Job job);
}
