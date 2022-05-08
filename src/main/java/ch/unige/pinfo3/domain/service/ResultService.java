package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.utils.QueryUtils;

import org.jboss.logging.Logger;

@ApplicationScoped
public class ResultService {
    @Inject 
    EntityManager em;

    @Inject
    Logger logger;

    @Inject
    QueryUtils qu;

    @Transactional
    public List<Result> getAll() {
        return qu.getAll(Result.class, em);
    }

    @Transactional
    public Optional<Result> getResult(String result_uuid) {
        return Optional.of(em.find(Result.class, result_uuid));
    }
}
