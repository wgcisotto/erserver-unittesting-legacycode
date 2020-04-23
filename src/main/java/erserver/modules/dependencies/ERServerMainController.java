package erserver.modules.dependencies;

import java.util.Timer;
import java.util.TimerTask;

public class ERServerMainController {

   private static StaffAssignment staffAssignment;
   private static InboundPatientSource inboundPatientSource;
   private static AlertScanner alertScanner;

   static {
      staffAssignment = new StaffAssignmentManager();
      EmergencyResponseService emergencyTransportService = new EmergencyResponseService("http://localhost", 4567, 1000);
      inboundPatientSource = new InboundPatientController(emergencyTransportService);
      alertScanner = new AlertScanner(inboundPatientSource);
      TimerTask alertTask = new TimerTask() {
         @Override
         public void run() {
            alertScanner.scan();
         }
      };
      Timer timer = new Timer();
      timer.schedule(alertTask, 1000, 30000);
   }

   public InboundPatientSource getInboundPatientController() {
      return inboundPatientSource;
   }

   public StaffAssignment getStaffAssignmentManager() {
      return staffAssignment;
   }



}
