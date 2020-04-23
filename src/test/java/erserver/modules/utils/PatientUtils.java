package erserver.modules.utils;

import erserver.modules.dependencies.Priority;
import erserver.modules.testtypes.Patient;

public class PatientUtils {

    private PatientUtils(){

    }

    public static Patient createTestPatient(int transportId, Priority priority, String condition){
        Patient patient = new Patient();
        patient.setTransportId(transportId);
        patient.setPriority(priority);
        patient.setCondition(condition);
        return patient;
    }

}
