package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;

public class AssignPatientToBedCommand {

   private StaffAssignment staffAssignment;
   private InboundPatientSource inboundPatientSource;

   public AssignPatientToBedCommand(StaffAssignment staffAssignment, InboundPatientSource inboundPatientSource) {
      this.staffAssignment = staffAssignment;
      this.inboundPatientSource = inboundPatientSource;
   }

   public void assignPatientToBed(int transportId, int bedId) {
      Bed bed = staffAssignment.getBedById(bedId);
      Patient patient = getPatientByTransport(transportId);
      staffAssignment.assignPatientToBed(patient, bed);
      inboundPatientSource.informOfPatientArrival(transportId);
   }

   private Patient getPatientByTransport(int transportId) {
      for (Patient patient : inboundPatientSource.currentInboundPatients()) {
         if (patient.getTransportId() == transportId) {
            return patient;
         }
      }
      throw new RuntimeException("Unable to find inbound patient " + transportId);
   }


}
