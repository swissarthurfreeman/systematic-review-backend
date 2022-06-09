package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Job;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static ch.unige.pinfo3.utils.RandomProducer.getRandomJob;

@QuarkusTest
public class JobTest {

    @Test
    void getUUIDTest(){
        Job job = new Job();

        Assertions.assertEquals(job.uuid, job.getUUID());
    }

    @Test
    void GetProgressFractionTest(){
        Job job = getRandomJob();

        if(job.totalTrials != 0){
            Assertions.assertEquals(job.getProgressFraction(), (double) job.progress/ (double)job.totalTrials);
        }
        else{
            Assertions.assertEquals(job.getProgressFraction(), 0);
        }

    }
}
