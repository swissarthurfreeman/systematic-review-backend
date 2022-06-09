package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.service.cnfUtils.CnfUtils;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static ch.unige.pinfo3.domain.service.cnfUtils.CnfUtils.createOrbitalFormula;

@QuarkusTest
public class CnfUtilsTest {


    @Test
    void createOrbitalFormulaNotTest(){
        Assertions.assertEquals(createOrbitalFormula("NOT test"), " ~  test");
    }
}
