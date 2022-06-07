package ch.unige.pinfo3.domain.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import com.github.javafaker.*;
import java.util.Random;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.utils.QueryUtils; 

@ApplicationScoped
public class ArticleService {
    @Inject 
    EntityManager em;

    static Random rand = new Random();

    @Transactional
    public List<Article> getArticlesOf(String result_uuid) {
        return QueryUtils.select(Article.class, "result_uuid", result_uuid, em);
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
}
