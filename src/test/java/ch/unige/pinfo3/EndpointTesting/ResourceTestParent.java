package ch.unige.pinfo3.EndpointTesting;

import ch.unige.pinfo3.domain.service.JobService;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.jwt.build.Jwt;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.UUID;


public class ResourceTestParent {

    @Inject
    EntityManager em;

    @InjectMock
    JobService js;

    protected String userUUID = UUID.randomUUID().toString();

    protected String getAccessToken(String userName) {
        return Jwt.preferredUserName(userName).claim("sub", userUUID).issuer("https://server.example.com")
                .audience("https://service.example.com").sign();
    }
}
