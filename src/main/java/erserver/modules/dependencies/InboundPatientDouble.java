package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InboundPatientDouble implements InboundPatientSource {

    private List<Patient> patients;

    public InboundPatientDouble(){
        patients = new ArrayList<>();
    }

    public void addNewInboundPatient(Patient patient){
        this.patients.add(patient);
    }

    @Override
    public List<Patient> currentInboundPatients() {
        return Collections.unmodifiableList(patients);
    }

    @Override
    public void informOfPatientArrival(int transportId) {
        // no need yet
    }
}
