package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Article;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ArticleTest {

    @Test
    void constructorTest(){
        Article article = new Article("uuid", "result_uuid", "Title", "pmcid", "authors", "abstract", "full_text", "url", "journal", "year", "date", "labels", "text", 1, 2.0, 3.0);

        Assertions.assertEquals(article.uuid, "uuid");
        Assertions.assertEquals(article.result_uuid, "result_uuid");
        Assertions.assertEquals(article.Title, "Title");
        Assertions.assertEquals(article.PMCID, "pmcid");
        Assertions.assertEquals(article.Authors, "authors");
        Assertions.assertEquals(article.Abstract, "abstract");
        Assertions.assertEquals(article.Full_text, "full_text");
        Assertions.assertEquals(article.URL, "url");
        Assertions.assertEquals(article.Journal, "journal");
        Assertions.assertEquals(article.Year, "year");
        Assertions.assertEquals(article.Date, "date");
        Assertions.assertEquals(article.labels, "labels");
        Assertions.assertEquals(article.text, "text");
        Assertions.assertEquals(article.cluster, 1);
        Assertions.assertEquals(article.x, 2.0);
        Assertions.assertEquals(article.y, 3.0);
    }
}
