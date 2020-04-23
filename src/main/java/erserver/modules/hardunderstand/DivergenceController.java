package erserver.modules.hardunderstand;

import erserver.modules.dependencies.*;
import erserver.modules.dependencies.vendorpagersystem.PagerSystem;
import erserver.modules.dependencies.vendorpagersystem.PagerTransport;
import erserver.modules.testtypes.Patient;

import java.util.List;
import java.util.stream.Collectors;

public class DivergenceController {

   private static final String ADMIN_ON_CALL_DEVICE = "111-111-1111";

   private boolean redDivergence;
   private boolean yellowDivergence;
   private boolean greenDivergence;
   private int redCount;
   private int yellowCount;
   private int greenCount;
   private int allowedCount;
   private int redBedOverFlowAllowed;
   private int yellowBedOverFlowAllowed;
   private int greenBedOverFlowAllowed;

   private int redInboundCount;
   private int yellowInboundCount;
   private int greenInboundCount;

   private StaffAssignment manager;
   private InboundPatientSource controller;
   private EmergencyResponseProvider transportService;
   private AlertTransmitter transport;

   public DivergenceController(InboundPatientSource controller, StaffAssignment manager,
                               EmergencyResponseProvider transportService, AlertTransmitter transport ) {
      this.redDivergence = false;
      this.yellowDivergence = false;
      this.greenDivergence = false;
      this.redCount = 0;
      this.yellowCount = 0;
      this.greenCount = 0;
      this.allowedCount = 3;
      this.redBedOverFlowAllowed = 0;
      this.yellowBedOverFlowAllowed = 1;
      this.greenBedOverFlowAllowed = 4;

      this.redInboundCount = 0;
      this.yellowInboundCount = 0;
      this.greenInboundCount = 0;

      this.controller = controller;
      this.manager = manager;
      this.transportService = transportService;
      this.transport = transport;
   }




   public void check() {
//      StaffAssignment manager = new ERServerMainController().getStaffAssignmentManager(); //this interface or inject as is
//      InboundPatientSource controller = new ERServerMainController().getInboundPatientController();//this InboundPatientSource
      int[] redStaffRequired = {1, 2};
      int[] yellowStaffRequired = {1, 1};
      int[] greenStaffRequired = {0, 1};
      boolean redIncremented = false;
      boolean yellowIncremented = false;
      boolean greenIncremented = false;

      List<Staff> staff = manager.getAvailableStaff();
      List<Bed> beds = manager.getAvailableBeds();


      int criticalBedsAvailable = calculateCriticalBedsAvailable(beds);

      determineInboundPatientsByPriority();

      int[] availableStaff = determineAvailablesDoctorsAndNurses(staff);

      // Increment redStaffRequired priority if not enough crit beds for inbound redStaffRequired priority patients
      if (redInboundCount > (criticalBedsAvailable + redBedOverFlowAllowed)) {
         redCount++;
         redIncremented = true;
      }

      // Increment greenStaffRequired and yellowStaffRequired diversion if not enough non crit beds available
      if (yellowInboundCount + greenInboundCount > (beds.size() - criticalBedsAvailable + yellowBedOverFlowAllowed + greenBedOverFlowAllowed)) {
         if ( (greenInboundCount > (beds.size() - criticalBedsAvailable + greenBedOverFlowAllowed)) && (yellowInboundCount <= (beds.size() - criticalBedsAvailable + yellowBedOverFlowAllowed)) ) {
            greenCount++;
            greenIncremented = true;
         } else {
            greenCount++;
            yellowCount++;
            greenIncremented = true;
            yellowIncremented = true;
         }
      }

      int[] neededStaff = new NeededStaffCalculator(redStaffRequired, yellowStaffRequired,
              greenStaffRequired, redInboundCount, yellowInboundCount, greenInboundCount).overall();

      // Determine if diversion increments need to occur based on available docs vs. needed docs
      if (neededStaff[0] > availableStaff[0]) {
         int diff = neededStaff[0] - availableStaff[0];
         if ((greenInboundCount * greenStaffRequired[0]) >= diff)  {
            if (!greenIncremented) {
               greenIncremented = true;
               greenCount++;
            }
         }
         else {
            int both = (yellowInboundCount * yellowStaffRequired[0]) + (greenInboundCount * greenStaffRequired[0]);
            if (both >= diff) {
               if (!greenIncremented) {
                  greenIncremented = true;
                  greenCount++;
               }
               if (!yellowIncremented) {
                  yellowIncremented = true;
                  yellowCount++;
               }
            }
            else {
               if (!greenIncremented) {
                  greenIncremented = true;
                  greenCount++;
               }
               if (!yellowIncremented) {
                  yellowIncremented = true;
                  yellowCount++;
               }
               if (!redIncremented) {
                  redIncremented = true;
                  redCount++;
               }
            }
         }
      }

      // Determine if diversion increments need to occur based on available nurses vs. needed nurses
      if (neededStaff[1] > availableStaff[1]) {
         int diff = neededStaff[1] - availableStaff[1];
         if ((greenInboundCount * greenStaffRequired[1]) >= diff)  {
            if (!greenIncremented) {
               greenIncremented = true;
               greenCount++;
            }
         }
         else {
            int both = (yellowInboundCount * yellowStaffRequired[1]) + (greenInboundCount * greenStaffRequired[1]);
            if (both >= diff) {
               if (!greenIncremented) {
                  greenIncremented = true;
                  greenCount++;
               }
               if (!yellowIncremented) {
                  yellowIncremented = true;
                  yellowCount++;
               }
            }
            else {
               if (!greenIncremented) {
                  greenIncremented = true;
                  greenCount++;
               }
               if (!yellowIncremented) {
                  yellowIncremented = true;
                  yellowCount++;
               }
               if (!redIncremented) {
                  redIncremented = true;
                  redCount++;
               }
            }
         }
      }

      DivergenceReportBuilder reportBuilder = new DivergenceReportBuilder(redInboundCount, yellowInboundCount,
              greenInboundCount, availableStaff, neededStaff, beds.size(), criticalBedsAvailable);

      String divergenceReport = reportBuilder.buildReport();

      // Go into diversion mode if divergence situation counts require it
      goIntoDiversionModeIfSituationRequires(redIncremented, yellowIncremented, greenIncremented, divergenceReport);
   }

   private int[] determineAvailablesDoctorsAndNurses(List<Staff> staff) {
      // Determine the number of availables doctors and nurses
      int[] availableStaff = {0, 0};
      for (Staff cur : staff) {
         if (StaffRole.DOCTOR.equals(cur.getRole())) {
            availableStaff[0]++;
         }
         else if (StaffRole.NURSE.equals(cur.getRole())) {
            availableStaff[1]++;
         }
      }
      return availableStaff;
   }

   private void determineInboundPatientsByPriority() {
      // Determine the number of inbound patients for each priority
      List<Patient> patients = patientsAffectingDivergence(controller.currentInboundPatients());
      for (Patient patient : patients) {
         if (Priority.RED.equals(patient.getPriority())) {
            redInboundCount++;
         }
         else if (Priority.YELLOW.equals(patient.getPriority())) {
            yellowInboundCount ++;
         }
         else if (Priority.GREEN.equals(patient.getPriority())) {
            greenInboundCount ++;
         }
      }
   }

   private void goIntoDiversionModeIfSituationRequires(boolean redIncremented, boolean yellowIncremented, boolean greenIncremented, String divergenceReport) {
//      transportService = new EmergencyResponseService("http://localhost", 4567, 1000); //this interface / abstract test double
      if (redIncremented) {
         if ((redCount > allowedCount) && !redDivergence) {
            redDivergence = true;
            transportService.requestInboundDiversion(Priority.RED);
            sendDivergencePage("Entered divergence for RED priority patients! " + divergenceReport,true);
            redCount = 0;
         }
      } else {
         redCount = 0;
         if (redDivergence) {
            transportService.removeInboundDiversion(Priority.RED);
            sendDivergencePage("Ended divergence for RED priority patients. " + divergenceReport,false);
            redDivergence = false;
         }
      }
      if (yellowIncremented) {
         if ((yellowCount > allowedCount) && !yellowDivergence) {
            yellowDivergence = true;
            transportService.requestInboundDiversion(Priority.YELLOW);
            sendDivergencePage("Entered divergence for YELLOW priority patients! " + divergenceReport,true);
            yellowCount = 0;
         }
      } else {
         yellowCount = 0;
         if (yellowDivergence) {
            transportService.removeInboundDiversion(Priority.YELLOW);
            sendDivergencePage("Ended divergence for YELLOW priority patients. " + divergenceReport, false);
            yellowDivergence = false;
         }
      }
      if (greenIncremented) {
         if ((greenCount > allowedCount) && !greenDivergence) {
            greenDivergence = true;
            transportService.requestInboundDiversion(Priority.GREEN);
            sendDivergencePage("Entered divergence for GREEN priority patients! " + divergenceReport, true);
            greenCount = 0;
         }
      } else {
         greenCount = 0;
         if (greenDivergence) {
            transportService.removeInboundDiversion(Priority.GREEN);
            sendDivergencePage("Ended divergence for GREEN priority patients. " + divergenceReport, false);
            greenDivergence = false;
         }
      }
   }

//   private int[] calculateNeededStaff(int[] redStaffRequired, int[] yellowStaffRequired, int[] greenStaffRequired,
//                                      int redInboundCount, int yellowInboundCount, int greenInboundCount) {
//      int[] neededStaff = {0, 0};
//      neededStaff[0] = redInboundCount * redStaffRequired[0];
//      neededStaff[0] += yellowInboundCount * yellowStaffRequired[0];
//      neededStaff[0] += greenInboundCount * greenStaffRequired[0];
//      neededStaff[1] = redInboundCount * redStaffRequired[1];
//      neededStaff[1] += yellowInboundCount * yellowStaffRequired[1];
//      neededStaff[1] += greenInboundCount * greenStaffRequired[1];
//      return neededStaff;
//   }

   protected int calculateCriticalBedsAvailable(List<Bed> beds) {
      int criticalBedsAvailable = 0;
      for (Bed bed : beds) {
         if (bed.isCriticalCare()) {
            criticalBedsAvailable ++;
         }
      }
      return criticalBedsAvailable;
   }

   //This alertTrasmitter interface
   private void sendDivergencePage(String text, boolean requireAck) {
      try {
         if (requireAck) {
            transport.transmitRequiringAcknowledgement(ADMIN_ON_CALL_DEVICE, text);
         } else {
            transport.transmit(ADMIN_ON_CALL_DEVICE, text);
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }

   public List<Patient> patientsAffectingDivergence(List<Patient> patients){
      return patients.stream()
              .filter( patient ->
                      !(patient.getCondition().toLowerCase().contains("ambulatory")
                        && patient.getCondition().toLowerCase().contains("non-emergency"))
                   || !(patient.getPriority().equals(Priority.GREEN))
               )
              .collect(Collectors.toList());
   }
}
