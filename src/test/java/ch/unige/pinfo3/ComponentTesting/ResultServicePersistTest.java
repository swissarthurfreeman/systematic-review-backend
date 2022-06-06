package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.service.ArticleService;
import ch.unige.pinfo3.domain.service.ResultService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ch.unige.pinfo3.domain.service.ArticleService.getRandomArticle;
import static ch.unige.pinfo3.domain.service.ResultService.getRandomResult;

@QuarkusTest
public class ResultServicePersistTest {

    @Inject
    ResultService rs;

    @Inject
    ArticleService as;

    Result  result = getRandomResult();

    @Test
    @Order(1)
    void persistResultTest(){

        rs.persist(result);

        Assertions.assertEquals(rs.getResult(result.uuid).toString(), Optional.of(result).toString());
    }

    @Test
    @Order(2)
    void persistArticleTest(){

        Article article = getRandomArticle(result.uuid);

        List<Article> articles = new ArrayList<Article>();
        articles.add(article);

        rs.persist(article);

        Assertions.assertEquals(as.getArticlesOf(result.uuid).get(0).toString(), articles.get(0).toString());


    }
}
