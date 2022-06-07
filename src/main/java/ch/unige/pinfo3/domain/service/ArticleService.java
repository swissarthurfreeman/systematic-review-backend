package ch.unige.pinfo3.domain.service;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.utils.QueryUtils; 

@ApplicationScoped
public class ArticleService {
    @Inject 
    EntityManager em;

    public List<Article> getArticlesOf(String result_uuid) {
        return QueryUtils.select(Article.class, "result_uuid", result_uuid, em);
    }
}
