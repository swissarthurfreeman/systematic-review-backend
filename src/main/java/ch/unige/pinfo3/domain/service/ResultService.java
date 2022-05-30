package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.core.Response;

import ch.unige.pinfo3.utils.ErrorReport;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.utils.QueryUtils;

import com.github.javafaker.Faker;

import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ResultService {
    @Inject 
    EntityManager em;

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

    @Incoming("clustered_articles")
    public void consume(String result) {
        System.out.println(result);
    }

    public static Result getRandomResult(){
        Faker fk = new Faker();
        Result result = new Result();
        result.uuid = UUID.randomUUID().toString();
        result.ucnf =  fk.expression("#{lorem.word} AND #{lorem.word} AND #{lorem.word}");
        return result;
    }
}
