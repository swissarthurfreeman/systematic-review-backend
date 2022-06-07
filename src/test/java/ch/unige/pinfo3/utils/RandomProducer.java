package ch.unige.pinfo3.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.github.javafaker.Faker;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.model.Search;

import java.security.SecureRandom;

public class RandomProducer {
    
    static SecureRandom rand = new SecureRandom();

    public static Job getRandomJob(){
        String[] status = {"queued"};
        Job job = new Job();
        Faker fk = new Faker();
        job.uuid = UUID.randomUUID().toString();
        job.ucnf = fk.expression("#{lorem.word} AND #{lorem.word} AND #{lorem.word}");
        job.timestamp = new Date().getTime();
        job.status = status[(int) (Math.random() * status.length)];
        return job;
    }

    public static Article getRandomArticle(String result_uuid) {
        Faker fk = new Faker();
        Article ar = new Article();
        ar.uuid = UUID.randomUUID().toString();
        ar.result_uuid= result_uuid;
        ar.Fulltext = fk.lorem().fixedString(1000);
        ar.Authors = fk.name().fullName() + ", " + fk.name().fullName();
        ar.Abstract = fk.lorem().fixedString(200);
        ar.Title = fk.book().title();
        ar.Url = fk.internet().url();
        ar.x = Math.abs(rand.nextGaussian(0.5, 0.3));
        ar.y = Math.abs(rand.nextGaussian(0.5, 0.5));
        return ar;
    }

    public static Result getRandomResult(){
        List<Article> articles = new ArrayList<Article>();
        Faker fk = new Faker();
        Result result = new Result();
        result.uuid = UUID.randomUUID().toString();
        result.ucnf =  fk.expression("#{lorem.word} AND #{lorem.word} AND #{lorem.word}");
        result.setArticles(articles);
        return result;
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
