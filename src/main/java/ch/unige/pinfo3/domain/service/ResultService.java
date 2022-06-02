package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.core.Response;

import ch.unige.pinfo3.utils.ErrorReport;
import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.utils.QueryUtils;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.vertx.core.json.JsonObject;

import com.github.javafaker.Faker;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;

@ApplicationScoped
public class ResultService {
    @Inject 
    EntityManager em;

    @Inject
    SearchService searchService;

    @Transactional
    public List<Result> getAll() {
        return QueryUtils.getAll(Result.class, em);
    }

    @Transactional
    public Optional<Result> getResult(String result_uuid) {
        return Optional.ofNullable(em.find(Result.class, result_uuid));
    }

    @Transactional
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
     * This method listens for incoming results on the clustered_articles channel.
     * @param result a json string to be parsed containing a ucnf key and a list 
     * of lists, see df_hover.json. 
     */
    @Transactional
    @Incoming("clustered_articles")
    public CompletableFuture<Void> consume(IncomingKafkaRecord<String, String> result) {
        String ucnf = result.getKey();
        Log.info("UCNF = " + ucnf);
        String data = result.getPayload();

        // create Result (articles reference it)
        Result res = new Result();
        res.uuid = UUID.randomUUID().toString();
        res.ucnf = ucnf;
        em.persist(res);
        searchService.updateSearchesOf(res.ucnf, res.uuid);
        
        // remove job for that ucnf (ucnf is unique col, so list has 1 element)
        List<Job> job = QueryUtils.select(Job.class, "ucnf", res.ucnf, em);
        if(job.size() == 1)
            em.remove(job.get(0));

        System.out.println(ucnf);
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            // parse json object
            json = (JSONObject) parser.parse(data);
            System.out.println(json.keySet());
            JSONObject titles = (JSONObject) json.get("Title");
            JSONObject pmcids = (JSONObject) json.get("PMCID");
            JSONObject authors = (JSONObject) json.get("Authors");
            JSONObject abstracts = (JSONObject) json.get("Abstract");
            JSONObject fullTexts = (JSONObject) json.get("Full_text");
            JSONObject urls = (JSONObject) json.get("URL");
            JSONObject journals = (JSONObject) json.get("Journal");
            JSONObject years = (JSONObject) json.get("Year");
            JSONObject dates = (JSONObject) json.get("Date");
            JSONObject labels = (JSONObject) json.get("labels");
            JSONObject texts = (JSONObject) json.get("text");
            JSONObject clusters = (JSONObject) json.get("cluster");
            JSONObject xs = (JSONObject) json.get("x");
            JSONObject ys = (JSONObject) json.get("y");

            int n = titles.size();
            for(int i=0; i < n; i++) {
                String article_number = Integer.toString(i);
                String title = String.valueOf(titles.get(article_number));                
                String label = String.valueOf(labels.get(article_number));

                String year = String.valueOf(years.get(article_number));
                var sep = year.split("-");
                Log.info(year);
                
                if(sep.length != 3)
                    year = "NA";
                
                String date = String.valueOf(dates.get(article_number));
                Log.info(year);
                
                int cluster = Integer.parseInt(String.valueOf(clusters.get(article_number)));
                
                String text =  String.valueOf(texts.get(article_number));
                
                String fullText = String.valueOf(fullTexts.get(article_number));
                fullText = fullText.substring(0, Math.min(100, fullText.length()));
                
                String abstra = String.valueOf(abstracts.get(article_number));
                abstra = abstra.substring(0, Math.min(100, abstra.length()));
                
                Article a = new Article(
                    UUID.randomUUID().toString(),
                    res.uuid,
                    title,
                    String.valueOf(pmcids.get(article_number)),
                    String.valueOf(authors.get(article_number)),
                    abstra,
                    fullText,
                    String.valueOf(urls.get(article_number)),
                    String.valueOf(journals.get(article_number)),
                    year,
                    date,
                    label,
                    text,
                    cluster,
                    Double.parseDouble(String.valueOf(xs.get(article_number))),
                    Double.parseDouble(String.valueOf(ys.get(article_number)))
                );
                em.persist(a);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result.ack().toCompletableFuture();
    }

    public static Result getRandomResult(){
        Faker fk = new Faker();
        Result result = new Result();
        result.uuid = UUID.randomUUID().toString();
        result.ucnf =  fk.expression("#{lorem.word} AND #{lorem.word} AND #{lorem.word}");
        return result;
    }
}
