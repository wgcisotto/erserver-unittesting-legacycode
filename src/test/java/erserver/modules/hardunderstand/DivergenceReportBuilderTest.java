package erserver.modules.hardunderstand;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DivergenceReportBuilderTest {


    private String expectedReport = "\nSituation Status:\n" +
            "Inbound patients requiring beds: 1 Red, 2 Yellow, 4 Green.\n" +
            "Available Docs/Nurses: 3/5\n" +
            "Needed Docs/Nurses: 3/8\n" +
            "Available Total Beds/Crit Beds: 10/4";

    @Test
    public void testReport() {
        DivergenceReportBuilder builder = new DivergenceReportBuilder(1, 2, 4, new int[]{3, 5}, new int[]{3, 8}, 10, 4);
        assertEquals(expectedReport, builder.buildReport());
    }

}