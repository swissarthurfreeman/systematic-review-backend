package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import java.util.List;
import ch.unige.pinfo3.domain.model.Result;
import org.eclipse.microprofile.context.ManagedExecutor;

@ApplicationScoped

// there can only be one jobber.
public class ResultService {
    @Inject 
    EntityManager em;

    @Inject
    ManagedExecutor executor;

    @Transactional
    public List<Result> getAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Result> criteria = builder.createQuery(Result.class);
		criteria.from(Result.class);
		return em.createQuery(criteria).getResultList();
    }
}
