package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.domain.model.Job;

import java.util.List;
import java.util.UUID;

public interface JobService {
    List<Job> getAll();
    Job get(UUID id);
    void create(Job job);
}
