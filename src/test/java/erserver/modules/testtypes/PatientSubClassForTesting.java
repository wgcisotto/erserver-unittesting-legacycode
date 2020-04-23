package erserver.modules.testtypes;

import java.time.LocalDate;

public class PatientSubClassForTesting extends Patient {

    private LocalDate currentDate;

    public PatientSubClassForTesting(){
        this.currentDate = LocalDate.now();
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    @Override
    protected LocalDate getSystemCurrentDate() {
        return currentDate;
    }
}
