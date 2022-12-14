package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.utils.CnfUtils;
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
        String query = CreateRandomCnf();
        String shuffledQuery = shuffleCnf(query);
        Assertions.assertEquals(sortCnf(query), sortCnf(shuffledQuery));
    }

    @Test
    void computeUcnfExceptionTest(){
        String query = "sfgsdvd&sv__!eq";
        Assertions.assertEquals(computeUcnf(query), query);
    }

    @Test
    void CnfUtilsClassTest(){
        new CnfUtils();
    }

}
