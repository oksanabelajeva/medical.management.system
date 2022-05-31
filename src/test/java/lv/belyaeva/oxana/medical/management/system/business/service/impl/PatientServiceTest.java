package lv.belyaeva.oxana.medical.management.system.business.service.impl;

import lv.belyaeva.oxana.medical.management.system.business.mapper.PatientMapper;
import lv.belyaeva.oxana.medical.management.system.business.repository.PatientRepository;
import lv.belyaeva.oxana.medical.management.system.business.repository.model.PatientDAO;
import lv.belyaeva.oxana.medical.management.system.model.Gender;
import lv.belyaeva.oxana.medical.management.system.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepositoryMock;
    @Mock
    private PatientMapper patientMapperMock;
    @InjectMocks
    private PatientServiceImpl patientServiceImplMock;

    private Patient patient;
    private Patient patientDuplicate;
    private PatientDAO patientDAO;
    private PatientDAO updatedPatientDAO;
    private List<PatientDAO> patientDAOList;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void init() {
        patient = createPatient(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", "020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.");
        patientDuplicate = createPatient(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", "020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.");
        patientDAO = createPatientDAO(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", "020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.");
        updatedPatientDAO = createPatientDAO(1L, "John", "Dallas", Enum.valueOf(Gender.class, Gender.MALE.name()),
                "1985-05-02", "020585-12345", "+37312345678", "London, UK",
                "2022-05-30", "2022-06-02", "Headache",
                "Bandage");
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
        assertThrows(HttpClientErrorException.class, () -> patientServiceImplMock.savePatient(patientDuplicate));
        verify(patientRepositoryMock, times(0)).save(patientDAO);
    }

    @Test
    void updatePatientAllParametersTest() throws Exception {
        patient.setName("John");
        patient.setSurname("Dallas");
        patient.setGender(Enum.valueOf(Gender.class, Gender.MALE.name()));
        patient.setDateOfBirth("1985-05-02");
        patient.setPersonalCode("020585-12345");
        patient.setPhoneNumber("+37312345678");
        patient.setResidingAddress("London, UK");
        patient.setGetToHospitalDate("2022-05-30");
        patient.setLeaveHospitalDate("2022-06-02");
        patient.setDiseaseInformation("Headache");
        patient.setConsumedMedicines("Bandage");
        patientServiceImplMock.updatePatient(patient);
        when(patientMapperMock.patientToPatientDAO(patient)).thenReturn(updatedPatientDAO);
        when(patientRepositoryMock.save(updatedPatientDAO)).thenReturn(updatedPatientDAO);

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
        Assertions.assertFalse(patientServiceImplMock.findPatientById(-1L).isPresent());
        verify(patientRepositoryMock, times(1)).findById(anyLong());
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

    private Patient createPatient(Long patientId, String name, String surname, Gender gender, String dateOfBirth,
                                  String personalCode, String phoneNumber, String residingAddress, String getToHospitalDate,
                                  String leaveHospitalDate, String diseaseInformation, String consumedMedicines) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setName(name);
        patient.setSurname(surname);
        patient.setGender(gender);
        patient.setDateOfBirth(dateOfBirth);
        patient.setPersonalCode(personalCode);
        patient.setPhoneNumber(phoneNumber);
        patient.setResidingAddress(residingAddress);
        patient.setGetToHospitalDate(getToHospitalDate);
        patient.setLeaveHospitalDate(leaveHospitalDate);
        patient.setDiseaseInformation(diseaseInformation);
        patient.setConsumedMedicines(consumedMedicines);
        return patient;
    }

    private PatientDAO createPatientDAO(Long patientId, String name, String surname, Gender gender, String dateOfBirth,
                                        String personalCode, String phoneNumber, String residingAddress, String getToHospitalDate,
                                        String leaveHospitalDate, String diseaseInformation, String consumedMedicines) {
        PatientDAO patientDAO = new PatientDAO();
        patientDAO.setPatientId(patientId);
        patientDAO.setName(name);
        patientDAO.setSurname(surname);
        patientDAO.setGender(gender);
        patientDAO.setDateOfBirth(dateOfBirth);
        patientDAO.setPersonalCode(personalCode);
        patientDAO.setPhoneNumber(phoneNumber);
        patientDAO.setResidingAddress(residingAddress);
        patientDAO.setGetToHospitalDate(getToHospitalDate);
        patientDAO.setLeaveHospitalDate(leaveHospitalDate);
        patientDAO.setDiseaseInformation(diseaseInformation);
        patientDAO.setConsumedMedicines(consumedMedicines);
        return patientDAO;
    }

    private List<PatientDAO> createPatientListDAO(PatientDAO patientDAO) {
        List<PatientDAO> patientDAOList = new ArrayList<>();
        patientDAOList.add(patientDAO);
        patientDAOList.add(patientDAO);
        return patientDAOList;
    }
}
