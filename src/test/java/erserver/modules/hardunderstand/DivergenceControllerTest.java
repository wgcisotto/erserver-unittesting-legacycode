package erserver.modules.hardunderstand;

import erserver.modules.dependencies.*;
import erserver.modules.testtypes.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static erserver.modules.utils.PatientUtils.createTestPatient;
import static org.junit.jupiter.api.Assertions.*;

class DivergenceControllerTest {

    private DivergenceController controller;
    private ArrayList<Patient> incomingPatients;

    @BeforeEach
    public void setUp() {
        InboundPatientDouble inboundPatientDouble = new InboundPatientDouble();
        inboundPatientDouble.addNewInboundPatient(createTestPatient(1, Priority.RED, "stroke"));
        inboundPatientDouble.addNewInboundPatient(createTestPatient(2, Priority.YELLOW, "mild stroke"));

        StaffAssignmentDouble staffAssignmentDouble = new StaffAssignmentDouble();

        controller = new DivergenceController(inboundPatientDouble, staffAssignmentDouble,
                new EmergencyResponseDouble(), new AlertTransmitterTestDouble());
        incomingPatients = new ArrayList<>();
    }

    @Test
    public void testGreenPatientsNotRequiringBedFiltered() {
        Patient nonEmerPatient = createPatient(Priority.GREEN, "non-emergency situation, patient is ambulatory");
        incomingPatients.add(nonEmerPatient);
        incomingPatients.add(createPatient(Priority.RED, "non-emergency situation, patient is ambulatory"));
        incomingPatients.add(createPatient(Priority.YELLOW, "non-emergency situation, patient is ambulatory"));
        incomingPatients.add(createPatient(Priority.GREEN, "ambulatory bleeding"));
        incomingPatients.add(createPatient(Priority.GREEN, "non-emergency, but unable to walk"));
        List<Patient> filteredList = controller.patientsAffectingDivergence(incomingPatients);
        assertEquals(4, filteredList.size());
        assertFalse(filteredList.contains(nonEmerPatient));
    }

    @Test
    public void testCheckMethod(){
        controller.check();
    }

    private Patient createPatient(Priority priority, String condition) {
        Patient patient = new Patient();
        patient.setPriority(priority);
        patient.setCondition(condition);
        return patient;
    }

    @Test
    void shouldCalculateCriticalBedsAvailable() {
        final List<Bed> beds = Arrays.asList(new Bed(1, false),
                new Bed(1, true), new Bed(3, false),
                new Bed(4, true));

        final int criticalBedsAvailable = controller.calculateCriticalBedsAvailable(beds);

        assertEquals(4, beds.size());
        assertEquals(2, criticalBedsAvailable);
    }

    @Test
    void shouldCalculateZeroCriticalBedsAvailable() {
        final List<Bed> beds = Arrays.asList(new Bed(1, false),
                new Bed(1, false), new Bed(3, false),
                new Bed(4, false));

        final int criticalBedsAvailable = controller.calculateCriticalBedsAvailable(beds);

        assertEquals(4, beds.size());
        assertEquals(0, criticalBedsAvailable);
    }



}