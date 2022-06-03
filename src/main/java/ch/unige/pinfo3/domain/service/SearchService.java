/**
 * This class contains core functionality for dealing
 * with searches. 
 */

package ch.unige.pinfo3.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import com.github.javafaker.Faker;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.utils.ErrorReport;
import io.quarkus.logging.Log;

import ch.unige.pinfo3.utils.QueryUtils; 

@ApplicationScoped
public class SearchService {
    @Inject
    EntityManager em;
    
    @Inject
    JobService jobService;

    /**
     * Create a search object linked to user.
     * @param search a search object containing a syntatically valid query and valid user_uuid.
     * @return the newly created search object. Succeeds no matter what. 
     * 
     * To Do :
     * - check syntax
     * - check search has valid user_uuid
     * - compute and assign ucnf form.
     */
    @Transactional
    public Search create(Search search) {
        search.timestamp = new Date().getTime();
        search.uuid = UUID.randomUUID().toString();
        search.ucnf = search.query;
        Log.info(search.user_uuid);
        // search for job or result with said ucnf
        List<Job> jobs = QueryUtils.select(Job.class, "ucnf", search.ucnf, em);
        List<Result> results = QueryUtils.select(Result.class, "ucnf", search.ucnf, em);

        if(!jobs.isEmpty()) {
            search.setJobUUID(jobs.get(0).uuid);
            search.setResultUUID(null);
        } else if(!results.isEmpty()) {
                search.setResultUUID(results.get(0).uuid);
                search.setJobUUID(null);
        } else {
            // submits job
            search.setJobUUID(jobService.submit(search.ucnf));
        }
        em.persist(search);
        return em.find(Search.class, search.uuid);
    }

    @Transactional
    public List<Search> getAll() {
        return QueryUtils.getAll(Search.class, em);
    }

    @Transactional
    public List<Search> getSearchesOf(String user_uuid) {
        return QueryUtils.select(Search.class, "user_uuid", user_uuid, em);
    }

    /***
     * Sets all searches pointing towards ucnf to have a nul job_uuid and 
     * sets resultUUIDof search to the newly obtained result. 
     */
    @Transactional
    public void updateSearchesOf(String ucnf, String result_uuid) {
        List<Search> searches = QueryUtils.select(Search.class, "ucnf", ucnf, em);
        for(Search el: searches) {
            el.setJobUUID(null);
            el.setResultUUID(result_uuid);
            em.persist(el);
        }
    }   

    /***
     * Perform syntactic analysis of query. 
     */
    public Optional<ErrorReport> syntaxAnalysis(String query) {
        // TODO use ANTLR java syntactic analyser to validate query.
        return Optional.empty();
    }

    public Search getSearchOfUser(String user_uuid, String search_uuid) {
        return QueryUtils.select(Search.class, "uuid", search_uuid,"user_uuid", user_uuid,  em).get(0); /// todo ICI IL Y AVAIT UNE ERREUR, LES COLONNES ETAIENT ECHANGES ET AU LIEU DE uuid IL Y AVAIT recherche_uuid, DONC NE TROUVAIT PAS LES RECHERCHES
    }

    public Optional<ErrorReport> checkExistence(String search_uuid) {
        var search = Optional.ofNullable(em.find(Search.class, search_uuid));
        if(search.isEmpty()) {
            var err = new ErrorReport();
            err.errors.add(
                new ErrorReport.Error(
                    "invalid search uuid", 
                    "uuid provided does not refer to a search, please try another", 
                    Response.Status.NOT_FOUND
                )
            );
            return Optional.of(err);
        }
        return Optional.empty();
    }

    public static Search getRandomSearch(String user_uuid, String job_uuid, String result_uuid){
        Search search = new Search();
        Faker fk = new Faker();
        search.uuid = UUID.randomUUID().toString();
        search.user_uuid = user_uuid;
        search.query = fk.lorem().word() + " AND " + fk.lorem().word() + " AND " + fk.lorem().word();
        search.ucnf = search.query; /// TODO tranformer les query en ucnf
        search.timestamp = new Date().getTime();
        search.job_uuid = job_uuid;
        search.setResultUUID(null);
        if(result_uuid != null){
            search.setResultUUID(result_uuid);
            search.setJobUUID(null);
        }

        return search;
    }
}
