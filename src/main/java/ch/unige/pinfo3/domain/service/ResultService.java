package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;
import ch.unige.pinfo3.domain.model.Result;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ResultService {
    @Inject 
    EntityManager em;

    @Inject
    Logger LOG;

    @Transactional
    public List<Result> getAll() {
        LOG.info("Attempting to get results from db");
        CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Result> criteria = builder.createQuery(Result.class);
		criteria.from(Result.class);
		return em.createQuery(criteria).getResultList();
    }

    @Transactional
    public Result getResult(String result_uuid) {
        return em.find(Result.class, result_uuid);
    }
}
