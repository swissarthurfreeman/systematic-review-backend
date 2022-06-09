package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.utils.ErrorReport;
import ch.unige.pinfo3.utils.QueryUtils;
import com.google.gson.Gson;
import io.quarkus.logging.Log;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ResultService {
    @Inject 
    EntityManager em;

    @Inject
    SearchService searchService;

    public List<Result> getAll() {
        return QueryUtils.getAll(Result.class, em);
    }

    public Optional<Result> getResult(String result_uuid) {
        return Optional.ofNullable(em.find(Result.class, result_uuid));
    }

    public Optional<ErrorReport> checkExistence(String result_uuid) {
        var res = Optional.ofNullable(em.find(Result.class, result_uuid));
        if(res.isPresent())
            return Optional.empty();
        
        var err = new ErrorReport();
        err.errors.add(
            new ErrorReport.Error(
                "Invalid result uuid",
                "A result with the specified uuid does not exist, try another one",
                Response.Status.NOT_FOUND
        ));
        return Optional.of(err);
    }

    /***
     * This method listens for incoming results on the Jarticles channel.
     * @param result a json string to be parsed containing a ucnf key and a list 
     * of lists, see df_hover.json. 
     */
    @Incoming("Jarticles")
    @Transactional
    public void consume(ConsumerRecord<String, String> result) {
        String ucnf = result.key();
        
        Gson g = new Gson();
        Article received_article = g.fromJson(result.value(), Article.class);
        Log.debug("Received article with Tile = " + received_article.Title);
        // create Result if not previously created (articles reference it)
        String res_uuid;
        List<Result> potential_res;
        
        potential_res = QueryUtils.select(Result.class, "ucnf", ucnf, em);
        
        if(potential_res.size() == 0) {     // if no result exists, create one. 
            Result res = new Result();    
            res.uuid = UUID.randomUUID().toString();
            res_uuid = res.uuid;
            res.ucnf = ucnf;
            em.persist(res);
            searchService.updateSearchesOf(res.ucnf, res.uuid);

            // remove job for that ucnf (ucnf is unique col, so list has 1 or 0 element)
            List<Job> job = QueryUtils.select(Job.class, "ucnf", ucnf, em);
            if(job.size() == 1)
                em.remove(job.get(0));
        } else {
            res_uuid = potential_res.get(0).uuid;
        }
        
        received_article.uuid = UUID.randomUUID().toString();
        received_article.result_uuid = res_uuid;
        em.persist(received_article);
    }
}
