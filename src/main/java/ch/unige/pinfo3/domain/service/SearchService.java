/**
 * This class contains core functionality for dealing
 * with searches. 
 */

package ch.unige.pinfo3.domain.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.model.Search;

@ApplicationScoped
public class SearchService {
    @Inject
    EntityManager em;

    @Inject
    JobService jobber;

    /**
     * Create a search object linked to user.
     * @param search a search object containing a query and valid user_uuid.
     * @return the newly created search object. Succeeds no matter what. 
     * 
     * To Do :
     * - check syntax
     * - check search has valid user_uuid
     * - compute and assign ucnf form.
     * - create association to job or result object.
     */
    @Transactional
    public Search create(Search search) {
        search.timestamp = new Date();
        search.uuid = UUID.randomUUID().toString();
        search.ucnf = search.query;
        
        // search for job or result with said ucnf
        // create association
        Job job = em.find(Job.class, search.ucnf);
        Result res = em.find(Result.class, search.ucnf);
        if(job != null) {
            search.setJobUUID(job.uuid);
            //search.result_uuid = null;
        } else if(res != null) {
                //search.result_uuid = res.uuid;
                search.setJobUUID(null);
        } else {
            search.setJobUUID(jobber.submit(search.ucnf));
        }
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

    @Transactional
    public List<Search> getSearchesOf(String user_uuid) {
        // building queries uses the builder design pattern.
        // the builder allows us to build a complex query (SQL under the hood)
        // we build the query once we've applied all the predicates. 
        CriteriaBuilder builder = em.getCriteriaBuilder();
        
        // create empty query which gets gradually built.
        CriteriaQuery<Search> criteriaQuery = builder.createQuery(Search.class);
        
        // allows accessing underlying sql schema columns. 
        Root<Search> searchItem = criteriaQuery.from(Search.class);

        // column of db user_uuid = user_uuid var parameter.
        Predicate userIdPredicate = builder.equal(searchItem.get("user_uuid"), user_uuid);

        // use previously defined Predicates to build the query.
        criteriaQuery.where(userIdPredicate);

        List<Search> userSearches = em.createQuery(criteriaQuery).getResultList();
        
        return userSearches;
    }
}
