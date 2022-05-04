package ch.unige.pinfo3.domain.service;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import ch.unige.pinfo3.domain.model.Article;

@ApplicationScoped
public class ArticleService {
    @Inject 
    EntityManager em;

    @Transactional
    public List<Article> getArticlesOf(String result_uuid) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Article> criteriaQuery = builder.createQuery(Article.class);
        Root<Article> searchItem = criteriaQuery.from(Article.class);
        criteriaQuery.where(builder.equal(searchItem.get("result_uuid"), result_uuid));
        List<Article> result_articles = em.createQuery(criteriaQuery).getResultList();
        return result_articles;
    }
}
