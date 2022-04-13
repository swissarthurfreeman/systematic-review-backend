package ch.unige.pinfo3.domain.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import ch.unige.pinfo3.domain.model.Search;

@ApplicationScoped
public class SearchService {
    @Inject
    EntityManager em;

    /**
     * Create a search object linked to user.
     * @param search a search object containing a query and valid user_uuid.
     * @return the newly created search object.
     * 
     * To Do :
     * - check search has valid user_uuid
     * - compute and assign ucnf form.
     */
    @Transactional
    public Search create(Search search) {
        search.uuid = UUID.randomUUID().toString();
        search.ucnf = search.query;
        em.persist(search);
        return em.find(Search.class, search.uuid);
    }

    @Transactional
    public List<Search> getAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Search> criteria = builder.createQuery(Search.class);
		criteria.from(Search.class);
		return em.createQuery(criteria).getResultList();
    }
}
