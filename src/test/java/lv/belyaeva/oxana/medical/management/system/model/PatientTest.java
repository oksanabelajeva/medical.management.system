package lv.belyaeva.oxana.medical.management.system.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ExtendWith(SpringExtension.class)
class PatientTest {

    private Patient patient;

    @BeforeEach
    public void setUp() throws Exception {
        patient = new Patient();
    }

    @Test
    void calculateAgeOfThePatientTest() {
        LocalDate dateOfBirthBeforeCurrentDate = LocalDate.of(2020, 1, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateOfBirthBeforeCurrentDate = dateOfBirthBeforeCurrentDate.format(formatter);
        patient.setDateOfBirth(formattedDateOfBirthBeforeCurrentDate);
        Assertions.assertEquals(2, patient.calculateAgeOfThePatient(dateOfBirthBeforeCurrentDate, LocalDate.now()));
    }

    @Test
    void calculateInvalidAgeOfThePatientTest() {
        LocalDate dateOfBirthBeforeCurrentDate = LocalDate.of(2023, 1, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateOfBirthBeforeCurrentDate = dateOfBirthBeforeCurrentDate.format(formatter);
        patient.setDateOfBirth(formattedDateOfBirthBeforeCurrentDate);
        Assertions.assertEquals(0, patient.calculateAgeOfThePatient(dateOfBirthBeforeCurrentDate, LocalDate.now()));
    }
}