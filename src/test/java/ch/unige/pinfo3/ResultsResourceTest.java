package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.service.ArticleService;
import ch.unige.pinfo3.domain.service.ResultService;
import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
/*
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResultsResourceTest {

    @Inject
    EntityManager em;

    Result result = ResultService.getRandomResult();
    Article article1 = ArticleService.getRandomArticle(result.uuid);
    Article article2 = ArticleService.getRandomArticle(result.uuid);

    // persisting a result to DB
    @Order(1)
    @Test
    @Transactional
    void persistResult(){
        Log.info("persisting a results and articles to DB");
        Log.info(em);
        em.persist(result);
        em.persist(ResultService.getRandomResult());
        em.persist(article1);
        em.persist(article2);
    }

    // Testing endpoint GET /results
    @Order(2)
    @Test
    void getResults() {
        Log.info("Testing endpoint GET /results");



        given()
                .when()
                .get("/results")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(2));
    }

    // Testing endpoint GET /results/:id
    @Order(3)
    @Test
    void TestSpecificResult() {

        Log.info("Testing endpoint GET /results/:id");
        given()
                .when()
                .get("results/" + result.uuid)
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(2))
                .and()
                .body("uuid", CoreMatchers.equalTo(result.uuid))
                .and()
                .body("ucnf", CoreMatchers.equalTo(result.ucnf));
    }

    // Testing endpoint GET /results/:id with invalid ID
    @Order(4)
    @Test
    void TestSpecificResultInvalidId() {

        Log.info("Testing endpoint GET /results/:id with invalid ID");
        given()
                .when()
                .get("results/1234")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(400));
    }

    // Testing endpoint GET /results/:id/articles
    @Order(5)
    @Test
    void TestSpecificResultArticles() {

        Log.info("Testing endpoint GET /results/:id/articles");
        given()
                .when()
                .get("results/"+result.uuid+"/articles")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(2)); /// Todo voir comment vérifier les éléments du premier (trouver le bon JSON path)


    }


}
*/