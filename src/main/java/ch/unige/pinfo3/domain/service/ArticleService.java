package ch.unige.pinfo3.domain.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import com.github.javafaker.*;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.utils.QueryUtils;

@ApplicationScoped
public class ArticleService {
    @Inject 
    EntityManager em;

    @Inject
    QueryUtils qu;

    @Transactional
    public List<Article> getArticlesOf(String result_uuid) {
        return qu.select(Article.class, "result_uuid", result_uuid, em);
    }
    
    public Article getRandomArticle(String result_uuid) {
        Faker fk = new Faker();
        Article ar = new Article();
        ar.uuid = UUID.randomUUID().toString();
        ar.result_uuid = result_uuid;
        ar.Full_text = fk.lorem().fixedString(1000);
        ar.Authors = fk.name().fullName() + ", " + fk.name().fullName();
        ar.Abstract = fk.lorem().fixedString(200);
        ar.Title = fk.book().title();
        ar.URL = fk.internet().url();
        ar.university = fk.university().name();
        return ar;
    }
}
