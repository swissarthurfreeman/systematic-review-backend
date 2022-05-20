package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.service.JobService;
import io.quarkus.test.junit.mockito.InjectMock;

import javax.inject.Inject;
import javax.persistence.EntityManager;


public class ResourceTestParent {

    @Inject
    EntityManager em;

    @InjectMock
    JobService js;
}
