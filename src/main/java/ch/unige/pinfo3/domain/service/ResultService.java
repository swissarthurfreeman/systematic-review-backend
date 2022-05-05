package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.utils.QueryUtils;

import org.jboss.logging.Logger;

@ApplicationScoped
public class ResultService {
    @Inject 
    EntityManager em;

    @Inject
    Logger LOG;

    @Inject
    QueryUtils qu;

    @Transactional
    public List<Result> getAll() {
        return qu.getAll(Result.class, em);
    }

    @Transactional
    public Result getResult(String result_uuid) {
        return em.find(Result.class, result_uuid);
    }
}
