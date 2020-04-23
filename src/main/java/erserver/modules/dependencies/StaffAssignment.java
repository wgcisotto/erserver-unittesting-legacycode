package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;

import java.util.List;

public interface StaffAssignment {

    List<Staff> getShiftStaff();

    List<Staff> getAvailableStaff();

    List<Staff> getPhysiciansOnDuty();

    List getBeds();

    List<Bed> getAvailableBeds();

    void assignPatientToBed(Patient patient, Bed bed);

    void assignStaffMemberToBed(Staff staff, Bed bed);

    Bed getBedById(int bedId);

    Staff getStaffById(int staffId);

}
