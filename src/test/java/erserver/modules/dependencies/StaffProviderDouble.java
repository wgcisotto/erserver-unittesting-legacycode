package erserver.modules.dependencies;

import java.util.ArrayList;
import java.util.List;

public class StaffProviderDouble implements StaffProvider {

    private List<Staff> staffToReturn;

    public StaffProviderDouble(){
        this.staffToReturn = new ArrayList<>();
    }

    public void addStaffMemberToReturn(Staff staff){
        this.staffToReturn.add(staff);
    }

    @Override
    public List<Staff> getShiftStaff() {
        return staffToReturn;
    }
}
