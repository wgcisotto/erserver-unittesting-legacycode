package erserver.modules.dependencies;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaffAssignmentManagerTest {

    @Test
    void physicianAndResidentsReturnForGetPhysiciansOnDuty(){
        final StaffProviderDouble staffRepo = new StaffProviderDouble();
        staffRepo.addStaffMemberToReturn(new Staff(1, "John Doctor", StaffRole.DOCTOR));
        staffRepo.addStaffMemberToReturn(new Staff(2, "Frank Resident", StaffRole.RESIDENT));
        StaffAssignment manager = new StaffAssignmentManager(staffRepo, new BedProviderDouble());
        final List<Staff> physiciansOnDuty = manager.getPhysiciansOnDuty();
        assertNotNull(physiciansOnDuty);
        assertEquals(2, physiciansOnDuty.size());
        assertEquals(1, physiciansOnDuty.get(0).getStaffId());
        assertEquals(2, physiciansOnDuty.get(1).getStaffId());
    }

    @Test
    void physicianAndResidentsReturnForGetPhysiciansOnDuty_WithMockito(){
        final StaffProvider staffProvider = Mockito.mock(StaffProvider.class);
        final BedProvider bedProvider = Mockito.mock(BedProvider.class);

        List<Staff> listStaff = new ArrayList<>();
        listStaff.add(new Staff(1, "John Doctor", StaffRole.DOCTOR));
        listStaff.add(new Staff(2, "Frank Resident", StaffRole.RESIDENT));

        List<Bed> listBed = new ArrayList<>();

        Mockito.when(staffProvider.getShiftStaff()).thenReturn(listStaff);
        Mockito.when(bedProvider.getAllBeds()).thenReturn(listBed);

        StaffAssignment manager = new StaffAssignmentManager(staffProvider, bedProvider);

        final List<Staff> physiciansOnDuty = manager.getPhysiciansOnDuty();
        assertNotNull(physiciansOnDuty);
        assertEquals(2, physiciansOnDuty.size());
        assertEquals(1, physiciansOnDuty.get(0).getStaffId());
        assertEquals(2, physiciansOnDuty.get(1).getStaffId());
    }


}