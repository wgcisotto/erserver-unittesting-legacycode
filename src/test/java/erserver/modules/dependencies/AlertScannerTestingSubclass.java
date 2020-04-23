package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlertScannerTestingSubclass extends AlertScanner {

    private List<Patient> criticalPatients;

    public AlertScannerTestingSubclass(InboundPatientSource inboundPatientSource) {
        super(inboundPatientSource);
        criticalPatients = new ArrayList<>();
    }

    @Override
    protected void alertForNewCriticalPatient(Patient patient) {
        criticalPatients.add(patient);
    }

    public List<Patient> getCritialPatients(){
        return Collections.unmodifiableList(criticalPatients);
    }


}
