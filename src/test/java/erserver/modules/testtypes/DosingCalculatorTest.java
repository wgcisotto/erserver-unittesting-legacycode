package erserver.modules.testtypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DosingCalculatorTest {

    private DosingCalculator dosingCalculator;
    private PatientSubClassForTesting patient;

    @BeforeEach
    public void setUp() {
        dosingCalculator = new DosingCalculator();
        patient = new PatientSubClassForTesting();
    }

    @Test
    public void returnsCorrectDosesForNeonateByDaysNotMonths() {
        //TODO: Review this test when possible.
        // Original value 0 altered to 2.5 ml since clone the project
        patient.setBirthDate(LocalDate.of(2016, 2, 28));
        patient.setCurrentDate(LocalDate.of(2016, 3, 28));
        String singleDose = dosingCalculator.getRecommendedSingleDose(patient, "Tylenol Oral Suspension");
        assertEquals("0", singleDose);
    }

    @Test
    public void returnsCorrectDosesForInfant() {
        patient.setBirthDate(LocalDate.now().minusDays(40));
        String singleDose = dosingCalculator.getRecommendedSingleDose(patient, "Tylenol Oral Suspension");
        assertEquals("2.5 ml", singleDose);
    }

    @Test
    public void returnsCorrectDosesForChild() {
        patient.setBirthDate(LocalDate.now().minusYears(3));
        String singleDose = dosingCalculator.getRecommendedSingleDose(patient, "Tylenol Oral Suspension");
        assertEquals("5 ml", singleDose);
    }

    @Test
    public void returnsCorrectDosesForNeonateAmox() {
        patient.setBirthDate(LocalDate.now().minusDays(20));
        String singleDose = dosingCalculator.getRecommendedSingleDose(patient, "Amoxicillin Oral Suspension");
        assertEquals("15 mg/kg", singleDose);
    }

    @Test
    public void returnsExceptionForAdults() {
        patient.setBirthDate(LocalDate.now().minusYears(16));
        assertThrows(RuntimeException.class, () ->
                dosingCalculator.getRecommendedSingleDose(patient, "Amoxicillin Oral Suspension"));
    }

    @Test
    public void returnsNullForUnrecognizedMedication() {
        patient.setBirthDate(LocalDate.now().minusYears(16));
        assertThrows(RuntimeException.class, () ->
                dosingCalculator.getRecommendedSingleDose(patient, "No Such Med"));
    }
}