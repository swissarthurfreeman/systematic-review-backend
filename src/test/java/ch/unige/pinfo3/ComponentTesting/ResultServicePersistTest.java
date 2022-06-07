package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.service.ArticleService;
import ch.unige.pinfo3.domain.service.ResultService;
import ch.unige.pinfo3.utils.RandomProducer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import javax.transaction.Transactional;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
public class ResultServicePersistTest {

    @Inject
    EntityManager em;

    @Inject
    ResultService rs;

    @Inject
    ArticleService as;

    Result  result = RandomProducer.getRandomResult();

    @Test
    @Transactional
    @Order(1)
    void persistResultTest(){
        em.persist(result);
        Assertions.assertEquals(rs.getResult(result.uuid).toString(), Optional.of(result).toString());
    }

    @Test
    @Transactional
    @Order(2)
    void persistArticleTest(){

        Article article = RandomProducer.getRandomArticle(result.uuid);

        List<Article> articles = new ArrayList<Article>();
        articles.add(article);

        em.persist(article);

        Assertions.assertEquals(as.getArticlesOf(result.uuid).get(0).toString(), articles.get(0).toString());


    }
}
