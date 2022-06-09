package ch.unige.pinfo3.ComponentTesting;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static ch.unige.pinfo3.utils.CnfUtils.*;
import static ch.unige.pinfo3.utils.RandomProducer.CreateRandomCnf;
import static ch.unige.pinfo3.utils.RandomProducer.shuffleCnf;

@QuarkusTest
public class CnfUtilsTest {

    @Test
    void createOrbitalFormulaNotTest() {
        Assertions.assertEquals(createOrbitalFormula("NOT test"), " ~  test");
    }

    @Test
    void sortCnfNonAtomicTest(){
        // 100 tests pour bien vérifier
        for(int i = 0; i < 100; i++) {
            String query = CreateRandomCnf();
            String shuffledQuery = shuffleCnf(query);
            Assertions.assertEquals(sortCnf(query), sortCnf(shuffledQuery));
        }
    }

    @Test
    void computeUcnfExceptionTest(){
        String query = "sfgsdvdsveq";
        Assertions.assertEquals(computeUcnf(query), query);
    }

}
