package erserver.modules.dependencies;

public class AssignStaffToBedCommand {

   private StaffAssignment staffAssignment;

   public AssignStaffToBedCommand(StaffAssignment staffAssignment) {
      this.staffAssignment = staffAssignment;
   }

   public void assignStaffToBed(int[] staffIds, int bedId) {
      for (int staffId : staffIds) {
         Staff staff = staffAssignment.getStaffById(staffId);
         Bed bed = staffAssignment.getBedById(bedId);
         staffAssignment.assignStaffMemberToBed(staff, bed);
      }
   }
}
