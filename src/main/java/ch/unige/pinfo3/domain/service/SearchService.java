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
    JobService jobService;

    /**
     * Create a search object linked to user.
     * @param search a search object containing a query and valid user_uuid.
     * @return the newly created search object. Succeeds no matter what. 
     * 
     * To Do :
     * - check syntax
     * - check search has valid user_uuid
     * - compute and assign ucnf form.
     */
    @Transactional
    public Search create(Search search) {
        search.timestamp = new Date();
        search.uuid = UUID.randomUUID().toString();
        search.ucnf = search.query;
        
        // search for job or result with said ucnf
        // create association
        // TODO create helper function to execute simple criteria generically.
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Job> criteriaQuery = builder.createQuery(Job.class);
        Root<Job> searchItem = criteriaQuery.from(Job.class);
        criteriaQuery.where(builder.equal(searchItem.get("ucnf"), search.ucnf));
        List<Job> jobs = em.createQuery(criteriaQuery).getResultList();
        
        CriteriaBuilder builder2 = em.getCriteriaBuilder();
        CriteriaQuery<Result> criteriaQuery2 = builder.createQuery(Result.class);
        Root<Result> searchItem2 = criteriaQuery2.from(Result.class);
        criteriaQuery2.where(builder2.equal(searchItem2.get("ucnf"), search.ucnf));
        List<Result> results = em.createQuery(criteriaQuery2).getResultList();
        
        if(jobs.size() == 1) {
            search.setJobUUID(jobs.get(0).uuid);
            search.setResultUUID(null);
        } else if(results.size() == 1) {
                search.setResultUUID(results.get(0).uuid);
                search.setJobUUID(null);
        } else {
            search.setJobUUID(jobService.submit(search.ucnf));
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

    /***
     * Sets all searches pointing towards ucnf to have a nul job_uuid and 
     * sets result_uuid of search to the newly obtained result. 
     */
    @Transactional
    public void updateSearchesOf(String ucnf, String result_uuid) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Search> criteriaQuery = builder.createQuery(Search.class);
        Root<Search> searchItem = criteriaQuery.from(Search.class);
        criteriaQuery.where(builder.equal(searchItem.get("ucnf"), ucnf));
        
        List<Search> searches = em.createQuery(criteriaQuery).getResultList();
        for(Search el: searches) {
            el.setJobUUID(null);
            el.setResultUUID(result_uuid);
            em.persist(el);
        }
    }   
}
