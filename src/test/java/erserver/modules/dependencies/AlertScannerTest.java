package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;
import erserver.modules.utils.PatientUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static erserver.modules.utils.PatientUtils.createTestPatient;

class AlertScannerTest {

    @Test
    void scanAlertsForPriorityRedPatients() {
        final InboundPatientDouble inboundPatientSource = new InboundPatientDouble();
        inboundPatientSource.addNewInboundPatient(createTestPatient(1, Priority.RED, "stroke"));
        inboundPatientSource.addNewInboundPatient(createTestPatient(2, Priority.YELLOW, "mild stroke"));

        AlertTransmitterTestDouble transmitterDouble = new AlertTransmitterTestDouble();

        AlertScanner scanner = new AlertScanner(inboundPatientSource, transmitterDouble);

        scanner.scan();
//        Assertions.assertEquals(1, scanner.getCritialPatients().size());
//        Assertions.assertEquals(1, scanner.getCritialPatients().get(0).getTransportId());

        Assertions.assertEquals(1, transmitterDouble.getAlertsReceivedRequiringAck().size());
        Assertions.assertEquals("111-111-1111: New inbound critical patient: 1",
                transmitterDouble.getAlertsReceivedRequiringAck().get(0));
    }

    @Test
    void scanAlertsForPriorityYellowHeartArrhythmiaPatientsWithExtractClass() {
        final InboundPatientDouble inboundPatientSource = new InboundPatientDouble();
        inboundPatientSource.addNewInboundPatient(createTestPatient(1, Priority.GREEN, "shortness of breath"));
        inboundPatientSource.addNewInboundPatient(createTestPatient(2, Priority.YELLOW, "heart arrhythmia"));

        AlertTransmitterTestDouble transmitterDouble = new AlertTransmitterTestDouble();

        AlertScanner scanner = new AlertScanner(inboundPatientSource, transmitterDouble);

        scanner.scan();

        Assertions.assertEquals(1, transmitterDouble.getAlertsReceived().size());
        Assertions.assertEquals("111-111-1111: New inbound critical patient: 2",
                transmitterDouble.getAlertsReceived().get(0));
    }

    @Test
    void justTransmitOnceForGivenInboundPatient() {
        final InboundPatientDouble inboundPatientSource = new InboundPatientDouble();
        inboundPatientSource.addNewInboundPatient(createTestPatient(1, Priority.GREEN, "shortness of breath"));
        inboundPatientSource.addNewInboundPatient(createTestPatient(2, Priority.YELLOW, "heart arrhythmia"));

        AlertTransmitterTestDouble transmitterDouble = new AlertTransmitterTestDouble();

        AlertScanner scanner = new AlertScanner(inboundPatientSource, transmitterDouble);

        scanner.scan();
        scanner.scan();

        Assertions.assertEquals(1, transmitterDouble.getAlertsReceived().size());
        Assertions.assertEquals("111-111-1111: New inbound critical patient: 2",
                transmitterDouble.getAlertsReceived().get(0));
    }

    @Test
    void scanAlertsForPriorityYellowHeartArrhythmiaPatients() {
        final InboundPatientDouble inboundPatientSource = new InboundPatientDouble();
        inboundPatientSource.addNewInboundPatient(createTestPatient(1, Priority.GREEN, "shortness of breath"));
        inboundPatientSource.addNewInboundPatient(createTestPatient(2, Priority.YELLOW, "heart arrhythmia"));
        AlertTransmitterTestDouble transmitterDouble = new AlertTransmitterTestDouble();
        AlertScannerTestingSubclass scanner = new AlertScannerTestingSubclass(inboundPatientSource);
        scanner.scan();
        Assertions.assertEquals(1, scanner.getCritialPatients().size());
        Assertions.assertEquals(2, scanner.getCritialPatients().get(0).getTransportId());
    }

}