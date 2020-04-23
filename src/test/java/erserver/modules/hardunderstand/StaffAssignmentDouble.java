package erserver.modules.hardunderstand;

import erserver.modules.dependencies.Bed;
import erserver.modules.dependencies.Staff;
import erserver.modules.dependencies.StaffAssignment;
import erserver.modules.dependencies.StaffProvider;
import erserver.modules.testtypes.Patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaffAssignmentDouble implements StaffAssignment {

    List<Staff> staffs;
    List<Bed> beds;

    public StaffAssignmentDouble(){
        staffs = new ArrayList<>();
        beds = new ArrayList<>();
    }

    public void addNewStaff(Staff staff){
        staffs.add(staff);
    }

    public void addNewBed(Bed bed){
        beds.add(bed);
    }

    @Override
    public List<Staff> getAvailableStaff() {
        return Collections.unmodifiableList(staffs);
    }

    @Override
    public List<Bed> getAvailableBeds() {
        return Collections.unmodifiableList(beds);
    }

    @Override
    public List<Staff> getShiftStaff() {
        return null;
    }

    @Override
    public List<Staff> getPhysiciansOnDuty() {
        return null;
    }

    @Override
    public List getBeds() {
        return null;
    }

    @Override
    public void assignPatientToBed(Patient patient, Bed bed) {

    }

    @Override
    public void assignStaffMemberToBed(Staff staff, Bed bed) {

    }

    @Override
    public Bed getBedById(int bedId) {
        return null;
    }

    @Override
    public Staff getStaffById(int staffId) {
        return null;
    }
}
