package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InboundPatientControllerTest {

    @Test
    void getPatientsFromXMLToListCorrectly(){
//        InboundPatientController controller = new InboundPatientController(null);
        String xmlForInbound = "<Inbound>\n" +
                "    <Patient>\n" +
                "        <TransportId>1</TransportId>\n" +
                "        <Name>John Doe</Name>\n" +
                "        <Condition>heart arrhythmia</Condition>\n" +
                "        <Priority>YELLOW</Priority>\n" +
                "        <Birthdate></Birthdate>\n" +
                "    </Patient>\n" +
                "</Inbound>";
//        final List<Patient> patients = controller.buildPatientsFromXML(xmlForInbound);
        final List<Patient> patients = InboundPatientController.buildPatientsFromXML(xmlForInbound);

        assertNotNull(patients);
        assertEquals(1, patients.size());
        final Patient patient = patients.get(0);
        assertEquals(1, patient.getTransportId());
        assertEquals("John Doe", patient.getName());
        assertEquals(Priority.YELLOW, patient.getPriority());
        assertNull(patient.getBirthDate());
        assertEquals("heart arrhythmia", patient.getCondition());


    }


}