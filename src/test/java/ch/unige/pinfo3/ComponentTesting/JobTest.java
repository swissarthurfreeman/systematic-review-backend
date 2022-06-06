package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Job;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class JobTest {

    @Test
    void getUUIDTest(){
        Job job = new Job();

        Assertions.assertEquals(job.uuid, job.getUUID());
    }
}
