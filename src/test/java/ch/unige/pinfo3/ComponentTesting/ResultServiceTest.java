package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.service.ResultService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.UUID;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTestResource(OidcWiremockTestResource.class)
public class ResultServiceTest {

    @InjectMock
    EntityManager em;

    @Inject
    ResultService rs;

    // testing ResultService.java checkExistence test

    @Test
    void checkExistencePresentTest(){
        String result_uuid = UUID.randomUUID().toString();
        Mockito.when(em.find(Result.class, result_uuid)).thenReturn(null);

        var result = rs.checkExistence(result_uuid);

        Assertions.assertTrue (result.isPresent());

    }

    @Test
    void checkExistenceNotPresentTest(){
        String result_uuid = UUID.randomUUID().toString();
        Mockito.when(em.find(Result.class, result_uuid)).thenReturn(new Result());

        var error = rs.checkExistence(result_uuid);

        Assertions.assertTrue (error.isEmpty());
    }
}
