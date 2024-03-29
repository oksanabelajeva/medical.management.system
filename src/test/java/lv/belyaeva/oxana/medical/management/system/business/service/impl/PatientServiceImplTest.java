package lv.belyaeva.oxana.medical.management.system.business.service.impl;

import lv.belyaeva.oxana.medical.management.system.business.mapper.PatientMapper;
import lv.belyaeva.oxana.medical.management.system.business.repository.PatientRepository;
import lv.belyaeva.oxana.medical.management.system.business.repository.model.PatientDAO;
import lv.belyaeva.oxana.medical.management.system.exception.PatientAlreadyExistsException;
import lv.belyaeva.oxana.medical.management.system.model.Gender;
import lv.belyaeva.oxana.medical.management.system.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class PatientServiceImplTest {

    @Spy
    private PatientRepository patientRepositoryMock;
    @Spy
    private PatientMapper patientMapperMock;
    @InjectMocks
    private PatientServiceImpl patientServiceImplMock;

    private Patient patient;
    private Patient patientDuplicate;
    private PatientDAO patientDAO;
    private PatientDAO updatedPatientDAO;
    private List<PatientDAO> patientDAOList;

    @BeforeEach
    private void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void init() throws Exception {
        patient = createPatient(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", 42L, "020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.", "");
        patientDuplicate = createPatient(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", 42L, "020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.", "");
        patientDAO = createPatientDAO(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", 42L, "020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.", "");
        updatedPatientDAO = createPatientDAO(1L, "John", "Dallas", Enum.valueOf(Gender.class, Gender.MALE.name()),
                "1985-05-02", 42L, "020585-12345", "+37312345678", "London, UK",
                "2022-05-30", "2022-06-02", "Headache", "Bandage",
                "");
        patientDAOList = createPatientListDAO(patientDAO);
    }

    @Test
    void savePatientTest() throws Exception {
        when(patientRepositoryMock.save(patientDAO)).thenReturn(patientDAO);
        when(patientMapperMock.patientDAOToPatient(patientDAO)).thenReturn(patient);
        when(patientMapperMock.patientToPatientDAO(patient)).thenReturn(patientDAO);
        patientServiceImplMock.savePatient(patient);
        verify(patientRepositoryMock, times(1)).save(patientDAO);
    }

    @Test
    void savePatientInvalidTest() throws Exception {
        when(patientRepositoryMock.save(patientDAO)).thenThrow(new IllegalArgumentException());
        when(patientMapperMock.patientToPatientDAO(patient)).thenReturn(patientDAO);
        Assertions.assertThrows(IllegalArgumentException.class, () -> patientServiceImplMock.savePatient(patient));
        verify(patientRepositoryMock, times(1)).save(patientDAO);
    }

    @Test
    void savePatientInvalidDuplicateTest() throws Exception {
        when(patientRepositoryMock.findAll()).thenReturn(patientDAOList);
        assertThrows(PatientAlreadyExistsException.class, () -> patientServiceImplMock.savePatient(patientDuplicate));
        verify(patientRepositoryMock, times(0)).save(patientDAO);
    }

    @Test
    void updatePatientAllParametersTest() throws Exception {
        patient.setName("John");
        patient.setSurname("Dallas");
        patient.setGender(Enum.valueOf(Gender.class, Gender.MALE.name()));
        patient.setDateOfBirth("1985-05-02");
        patient.setAge(37L);
        patient.setPersonalCode("020585-12345");
        patient.setPhoneNumber("+37312345678");
        patient.setResidingAddress("London, UK");
        patient.setGetToHospitalDate("2022-05-30");
        patient.setLeaveHospitalDate("2022-06-02");
        patient.setDiseaseInformation("Headache");
        patient.setConsumedMedicines("Bandage");
        patient.setConsumedMedicines("Please remember to change bandage.");
        when(patientMapperMock.patientToPatientDAO(patient)).thenReturn(updatedPatientDAO);
        when(patientRepositoryMock.save(updatedPatientDAO)).thenReturn(updatedPatientDAO);
        when(patientRepositoryMock.findById(patient.getPatientId())).thenReturn(Optional.of(patientDAO));

        patientServiceImplMock.updatePatient(patient);

        assertEquals(updatedPatientDAO.getName(), patient.getName());
        assertEquals(updatedPatientDAO.getPatientId(), patient.getPatientId());
    }

    @Test
    void findAllPatientsTest() throws Exception {
        when(patientRepositoryMock.findAll()).thenReturn(patientDAOList);
        when(patientMapperMock.patientDAOToPatient(patientDAO)).thenReturn(patient);
        List<Patient> patientList = patientServiceImplMock.findAllPatients();
        Assertions.assertEquals(2, patientList.size());
        verify(patientRepositoryMock, times(1)).findAll();
    }

    @Test
    void findPatientByIdTest() throws Exception {
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.of(patientDAO));
        when(patientMapperMock.patientDAOToPatient(patientDAO)).thenReturn(patient);
        Optional<Patient> returnedPatient = patientServiceImplMock.findPatientById(patient.getPatientId());
        assertEquals(patient.getPatientId(), returnedPatient.get().getPatientId());
        verify(patientRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    void findPatientByIdInvalidTest() throws Exception {
        when(patientRepositoryMock.findById(-1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> patientServiceImplMock.findPatientById(-1L));
        verify(patientRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    void findPatientByGenderTest() {
        when(patientRepositoryMock.findAllPatientsByGender(Gender.FEMALE)).thenReturn(patientDAOList);
        when(patientMapperMock.patientDAOToPatient(patientDAO)).thenReturn(patient);
        List<Patient> patientList = patientServiceImplMock.findAllPatientsByGender(Gender.FEMALE);
        assertEquals(2, patientList.size());
        verify(patientRepositoryMock, times(1)).findAllPatientsByGender(Gender.FEMALE);
    }

    @Test
    void findPatientByGenderInvalidTest() {
        when(patientRepositoryMock.findAllPatientsByGender(Gender.FEMALE)).thenReturn(Collections.emptyList());
        assertTrue(patientServiceImplMock.findAllPatientsByGender(Gender.FEMALE).isEmpty());
        verify(patientRepositoryMock, times(1)).findAllPatientsByGender(Gender.FEMALE);
    }

    @Test
    void deletePatientByIdTest() {
        patientServiceImplMock.deletePatientById(1L);
        verify(patientRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void deletePatientByIdNullTest() throws Exception {
        doThrow(new NoSuchElementException()).when(patientRepositoryMock).deleteById(null);
        assertThrows(NoSuchElementException.class, () -> patientServiceImplMock.deletePatientById(null));
    }

    @Test
    void deletePatientByIdNegativeTest() throws Exception {
        doThrow(new NoSuchElementException()).when(patientRepositoryMock).deleteById(-1L);
        assertThrows(NoSuchElementException.class, () -> patientServiceImplMock.deletePatientById(-1L));
    }

    @Test
    void checkIfCurrentDateIsBeforeDateOfBirth() {
        try {
            LocalDate dateOfBirthAfterCurrentDate = LocalDate.of(2023, 1, 1);
            LocalDate currentDate = LocalDate.now();
            patientServiceImplMock.checkIfCurrentDateIsBeforeDateOfBirth(dateOfBirthAfterCurrentDate, currentDate);
        } catch (Exception exception) {
            assertEquals("Please insert valid date of birth, the patient is not born yet.", exception.getMessage());
        }
    }

    @Test
    void calculateAgeOfThePatientTest() {
        LocalDate dateOfBirthBeforeCurrentDate = LocalDate.of(2020, 12, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateOfBirthBeforeCurrentDate = dateOfBirthBeforeCurrentDate.format(formatter);
        patient.setDateOfBirth(formattedDateOfBirthBeforeCurrentDate);
        Assertions.assertEquals(2, patientServiceImplMock.calculateAgeOfThePatient(dateOfBirthBeforeCurrentDate,
                LocalDate.now()));
    }

    @Test
    void calculateInvalidAgeOfThePatientTest() {
        LocalDate dateOfBirthBeforeCurrentDate = LocalDate.of(2025, 12, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateOfBirthBeforeCurrentDate = dateOfBirthBeforeCurrentDate.format(formatter);
        patient.setDateOfBirth(formattedDateOfBirthBeforeCurrentDate);
        Assertions.assertEquals(-2, patientServiceImplMock.calculateAgeOfThePatient(dateOfBirthBeforeCurrentDate,
                LocalDate.now()));
    }

    private Patient createPatient(Long patientId, String name, String surname, Gender gender, String dateOfBirth, Long age,
                                  String personalCode, String phoneNumber, String residingAddress, String getToHospitalDate,
                                  String leaveHospitalDate, String diseaseInformation, String consumedMedicines,
                                  String warningInformation) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setName(name);
        patient.setSurname(surname);
        patient.setGender(gender);
        patient.setDateOfBirth(dateOfBirth);
        patient.setAge(age);
        patient.setPersonalCode(personalCode);
        patient.setPhoneNumber(phoneNumber);
        patient.setResidingAddress(residingAddress);
        patient.setGetToHospitalDate(getToHospitalDate);
        patient.setLeaveHospitalDate(leaveHospitalDate);
        patient.setDiseaseInformation(diseaseInformation);
        patient.setConsumedMedicines(consumedMedicines);
        patient.setWarningInformation(warningInformation);
        return patient;
    }

    private PatientDAO createPatientDAO(Long patientId, String name, String surname, Gender gender, String dateOfBirth,
                                        Long age, String personalCode, String phoneNumber, String residingAddress,
                                        String getToHospitalDate, String leaveHospitalDate, String diseaseInformation,
                                        String consumedMedicines, String warningInformation) {
        PatientDAO patientDAO = new PatientDAO();
        patientDAO.setPatientId(patientId);
        patientDAO.setName(name);
        patientDAO.setSurname(surname);
        patientDAO.setGender(gender);
        patientDAO.setDateOfBirth(dateOfBirth);
        patientDAO.setAge(age);
        patientDAO.setPersonalCode(personalCode);
        patientDAO.setPhoneNumber(phoneNumber);
        patientDAO.setResidingAddress(residingAddress);
        patientDAO.setGetToHospitalDate(getToHospitalDate);
        patientDAO.setLeaveHospitalDate(leaveHospitalDate);
        patientDAO.setDiseaseInformation(diseaseInformation);
        patientDAO.setConsumedMedicines(consumedMedicines);
        patientDAO.setWarningInformation(warningInformation);
        return patientDAO;
    }

    private List<PatientDAO> createPatientListDAO(PatientDAO patientDAO) {
        List<PatientDAO> patientDAOList = new ArrayList<>();
        patientDAOList.add(patientDAO);
        patientDAOList.add(patientDAO);
        return patientDAOList;
    }
}
