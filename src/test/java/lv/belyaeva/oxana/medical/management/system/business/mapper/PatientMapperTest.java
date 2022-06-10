package lv.belyaeva.oxana.medical.management.system.business.mapper;

import lv.belyaeva.oxana.medical.management.system.business.repository.model.PatientDAO;
import lv.belyaeva.oxana.medical.management.system.model.Gender;
import lv.belyaeva.oxana.medical.management.system.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatientMapperTest {

    @Mock
    private PatientMapperImpl patientMapperMock;

    private Patient patient;
    private PatientDAO patientDAO;

    @BeforeEach
    public void init() throws Exception {
        patientMapperMock = new PatientMapperImpl();
        patient = createPatient(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", 42L, "020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.", "");
        patientDAO = createPatientDAO(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", 42L, "020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.", "");
    }

    @Test
    void patientToPatientDAOTest() {
        patientMapperMock.patientToPatientDAO(patient);
        assertEquals(patientDAO.getPatientId(), patient.getPatientId());
        assertEquals(patientDAO.getName(), patient.getName());
        assertEquals(patientDAO.getSurname(), patient.getSurname());
        assertEquals(patientDAO.getGender(), patient.getGender());
        assertEquals(patientDAO.getDateOfBirth(), patient.getDateOfBirth());
        assertEquals(patientDAO.getAge(), patient.getAge());
        assertEquals(patientDAO.getPersonalCode(), patient.getPersonalCode());
        assertEquals(patientDAO.getPhoneNumber(), patient.getPhoneNumber());
        assertEquals(patientDAO.getResidingAddress(), patient.getResidingAddress());
        assertEquals(patientDAO.getGetToHospitalDate(), patient.getGetToHospitalDate());
        assertEquals(patientDAO.getLeaveHospitalDate(), patient.getLeaveHospitalDate());
        assertEquals(patientDAO.getDiseaseInformation(), patient.getDiseaseInformation());
        assertEquals(patientDAO.getConsumedMedicines(), patient.getConsumedMedicines());
        assertEquals(patientDAO.getWarningInformation(), patient.getWarningInformation());
    }

    @Test
    void patientNullToPatientDAOTest() {
        Patient patient = null;
        PatientDAO patientDAO = patientMapperMock.patientToPatientDAO(patient);
        assertEquals(null, patientDAO);
    }

    @Test
    void patientDAOToPatientTest() {
        patientMapperMock.patientDAOToPatient(patientDAO);
        assertEquals(patient.getPatientId(), patientDAO.getPatientId());
        assertEquals(patient.getName(), patientDAO.getName());
        assertEquals(patient.getSurname(), patientDAO.getSurname());
        assertEquals(patient.getGender(), patientDAO.getGender());
        assertEquals(patient.getDateOfBirth(), patientDAO.getDateOfBirth());
        assertEquals(patient.getAge(), patientDAO.getAge());
        assertEquals(patient.getPersonalCode(), patientDAO.getPersonalCode());
        assertEquals(patient.getPhoneNumber(), patientDAO.getPhoneNumber());
        assertEquals(patient.getResidingAddress(), patientDAO.getResidingAddress());
        assertEquals(patient.getGetToHospitalDate(), patientDAO.getGetToHospitalDate());
        assertEquals(patient.getLeaveHospitalDate(), patientDAO.getLeaveHospitalDate());
        assertEquals(patient.getDiseaseInformation(), patientDAO.getDiseaseInformation());
        assertEquals(patient.getConsumedMedicines(), patientDAO.getConsumedMedicines());
        assertEquals(patient.getWarningInformation(), patientDAO.getWarningInformation());
    }

    @Test
    void patientDAONullToPatientTest() {
        PatientDAO patientDAO = null;
        Patient patient = patientMapperMock.patientDAOToPatient(patientDAO);
        assertEquals(null, patient);
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
}