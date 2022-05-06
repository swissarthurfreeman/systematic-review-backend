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
import javax.transaction.Transactional;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.utils.QueryUtils;

@ApplicationScoped
public class SearchService {
    @Inject
    EntityManager em;

    @Inject
    JobService jobService;

    @Inject
    QueryUtils qu;

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
        List<Job> jobs = qu.select(Job.class, "ucnf", search.ucnf, em);
        List<Result> results = qu.select(Result.class, "ucnf", search.ucnf, em);
        
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
        return qu.getAll(Search.class, em);
    }

    @Transactional
    public List<Search> getSearchesOf(String user_uuid) {
        return qu.select(Search.class, "user_uuid", user_uuid, em);
    }

    /***
     * Sets all searches pointing towards ucnf to have a nul job_uuid and 
     * sets result_uuid of search to the newly obtained result. 
     */
    @Transactional
    public void updateSearchesOf(String ucnf, String result_uuid) {
        List<Search> searches = qu.select(Search.class, "ucnf", ucnf, em);
        for(Search el: searches) {
            el.setJobUUID(null);
            el.setResultUUID(result_uuid);
            em.persist(el);
        }
    }   
}
