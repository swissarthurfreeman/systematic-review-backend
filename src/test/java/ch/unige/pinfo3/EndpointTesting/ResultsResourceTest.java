package ch.unige.pinfo3.EndpointTesting;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.utils.RandomProducer;
import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;

import javax.transaction.Transactional;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
public class ResultsResourceTest extends ResourceTestParent{

    Result result = RandomProducer.getRandomResult();
    Article article1 = RandomProducer.getRandomArticle(result.uuid);
    Article article2 = RandomProducer.getRandomArticle(result.uuid);
    

    // persisting a result to DB
    @Order(1)
    @Test
    @Transactional
    void persistResult(){
        Log.info("persisting a results and articles to DB");
        Log.info(em);
        em.persist(result);
        em.persist(RandomProducer.getRandomResult());
        em.persist(article1);
        em.persist(article2);
    }

    // Testing endpoint GET /results
    @Order(2)
    @Test
    void getResults() {
        Log.info("Testing endpoint GET /results");

        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/results")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(4));
    }

    // Testing endpoint GET /results/:id
    @Order(3)
    @Test
    void TestSpecificResult() {

        Log.info("Testing endpoint GET /results/:id");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
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
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("results/1234")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(400))
                .and()
                .body("size()", CoreMatchers.equalTo(2));
    }

    // Testing endpoint GET /results/:id with inexistant ID
    @Order(4)
    @Test
    void TestSpecificResultInexistantId() {

        Log.info("Testing endpoint GET /results/:id with invalid ID");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("results/"+UUID.randomUUID().toString())
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(404))
                .and()
                .body("size()", CoreMatchers.equalTo(2));
    }

    // Testing endpoint GET /results/:id/articles
    @Order(5)
    @Test
    void TestSpecificResultArticles() {

        Log.info("Testing endpoint GET /results/:id/articles");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("results/"+result.uuid+"/articles")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(2))
                .and()
                .body("[0].size()", CoreMatchers.equalTo(15))
                .and()
                .body("[0][\"uuid\"]", CoreMatchers.equalTo(article1.uuid))
                .and()
                .body("[0][\"result_uuid\"]", CoreMatchers.equalTo(article1.result_uuid))
                .and()
                .body("[0][\"labels\"]", CoreMatchers.equalTo(article1.labels))
                .and()
                .body("[0][\"cluster\"]", CoreMatchers.equalTo(article1.cluster))
                .and()
                .body("[0][\"x\"]", CoreMatchers.equalTo(((float) article1.x)))
                .and()
                .body("[0][\"y\"]", CoreMatchers.equalTo((float) article1.y))
                .and()
                .body("[0][\"url\"]", CoreMatchers.equalTo(article1.Url))
                .and()
                .body("[0][\"published\"]", CoreMatchers.equalTo(article1.Published))
                .and()
                .body("[0][\"fulltext\"]", CoreMatchers.equalTo(article1.Fulltext))
                .and()
                .body("[0][\"title\"]", CoreMatchers.equalTo(article1.Title))
                .and()
                .body("[0][\"journal\"]", CoreMatchers.equalTo(article1.Journal))
                .and()
                .body("[0][\"PmcId\"]", CoreMatchers.equalTo(article1.PmcId))
                .and()
                .body("[0][\"doi\"]", CoreMatchers.equalTo(article1.DOI))
                .and()
                .body("[0][\"authors\"]", CoreMatchers.equalTo(article1.Authors));
    }

    @Order(6)
    @Test
    void TestInexistantResultArticles() {

        Log.info("Testing endpoint GET /results/:id/articles with inexistant id");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("results/"+UUID.randomUUID().toString()+"/articles")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(404))
                .and()
                .body("size()", CoreMatchers.equalTo(2));
    }
}
