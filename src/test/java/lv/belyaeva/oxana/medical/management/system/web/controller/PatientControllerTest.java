package lv.belyaeva.oxana.medical.management.system.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.belyaeva.oxana.medical.management.system.business.repository.PatientRepository;
import lv.belyaeva.oxana.medical.management.system.business.service.PatientService;
import lv.belyaeva.oxana.medical.management.system.model.Gender;
import lv.belyaeva.oxana.medical.management.system.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    public static String URL = "/api/v1/patient";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PatientController patientController;
    @MockBean
    private PatientRepository patientRepository;
    @MockBean
    private PatientService patientService;
    @MockBean
    private KieSession kieSession;

    private Patient patient;
    private List<Patient> patientList;

    @BeforeEach
    public void init()  throws Exception {
        patient = createPatient(1L, "Karina", "Kidman", Enum.valueOf(Gender.class, Gender.FEMALE.name()),
                "1980-05-02", 42L,"020580-12345", "+37112345678", "Riga, Latvia",
                "2022-05-27", "2022-06-10", "Flue",
                "Antibiotics, tea with honey.", "No warnings.");
        patientList = createPatientList(patient);
    }

    @Test
    void postPatientTest() throws Exception {

        when(patientService.savePatient(patient)).thenReturn(patient);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(patientService, times(1)).savePatient(patient);
    }

    @Test
    void postPatientInvalidNameTest() throws Exception {
        patient.setName(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidSurnameTest() throws Exception {
        patient.setSurname(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidGenderTest() throws Exception {
        patient.setGender(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidDateOfBirthTest() throws Exception {
        patient.setDateOfBirth(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidPersonalCodeTest() throws Exception {
        patient.setPersonalCode(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidPhoneNumberTest() throws Exception {
        patient.setPhoneNumber(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidResidingAddressTest() throws Exception {
        patient.setResidingAddress(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidGetToHospitalDateTest() throws Exception {
        patient.setGetToHospitalDate(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidLeaveHospitalDateTest() throws Exception {
        patient.setLeaveHospitalDate(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidDiseaseInformationTest() throws Exception {
        patient.setDiseaseInformation(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void postPatientInvalidConsumedMedicinesTest() throws Exception {
        patient.setConsumedMedicines(null);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(0)).savePatient(patient);
    }

    @Test
    void putPatientByIdTest() throws Exception {

        when(patientService.findPatientById(patient.getPatientId())).thenReturn(Optional.of(patient));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Karina"))
                .andExpect(status().isCreated());

        verify(patientService, times(1)).updatePatient(patient);
    }

    @Test
    void putPatientByIdNotFoundTest() throws Exception {

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/2")
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(patientService, times(0)).updatePatient(patient);
    }

    @Test
    void getAllPatientsTest() throws Exception {

        when(patientService.findAllPatients()).thenReturn(patientList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].patientId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Karina"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("Kidman"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("FEMALE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value("1980-05-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(42L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].personalCode").value("020580-12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber").value("+37112345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].residingAddress").value("Riga, Latvia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].getToHospitalDate").value("2022-05-27"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].leaveHospitalDate").value("2022-06-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].diseaseInformation").value("Flue"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].consumedMedicines").value("Antibiotics, tea with honey."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].warningInformation").value("No warnings."))
                .andExpect(status().isOk());

        verify(patientService, times(1)).findAllPatients();
    }

    @Test
    void getAllPatientsTestInvalid() throws Exception {
        patientList.clear();

        when(patientService.findAllPatients()).thenReturn(patientList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .content(asJsonString(patientList))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(patientService, times(1)).findAllPatients();
    }

    @Test
    void getPatientByIdTest() throws Exception {

        when(patientService.findPatientById(1L)).thenReturn(Optional.of(patient));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Karina"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Kidman"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("FEMALE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value("1980-05-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(42L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personalCode").value("020580-12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+37112345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.residingAddress").value("Riga, Latvia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.getToHospitalDate").value("2022-05-27"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.leaveHospitalDate").value("2022-06-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.diseaseInformation").value("Flue"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.consumedMedicines").value("Antibiotics, tea with honey."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.warningInformation").value("No warnings."))
                .andExpect(status().isOk());

        verify(patientService, times(1)).findPatientById(1L);
    }

    @Test
    void getPatientByIdNotFoundTest() throws Exception {

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/2")
                        .content(asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(patientService, times(0)).findPatientById(null);
    }

    @Test
    void getPatientsListByGenderTest() throws Exception {
        when(patientService.findAllPatientsByGender(Gender.FEMALE)).thenReturn(patientList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/filteredByGenderList/" + Gender.valueOf("FEMALE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].patientId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value("FEMALE"))
                .andExpect(status().isOk());

        verify(patientService, times(1)).findAllPatientsByGender(Gender.FEMALE);
    }

    @Test
    void getPatientsListByGenderNotFoundTest() throws Exception {
        when(patientService.findAllPatientsByGender(Gender.FEMALE)).thenReturn(Collections.emptyList());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/filteredByGenderList/" + Gender.valueOf("FEMALE"))
                        .content(asJsonString(Collections.emptyList()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(patientService, times(1)).findAllPatientsByGender(Gender.FEMALE);
    }

    @Test
    public void deletePatientByIdTest() throws Exception {
        when(patientService.findPatientById(1L)).thenReturn(Optional.of(patient));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePatientByIdNegativeTest() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePatientByIdNullTest() throws Exception {
        when(patientRepository.existsById(null)).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + null)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
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

    private List<Patient> createPatientList(Patient patient) {
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);
        patientList.add(patient);
        return patientList;
    }

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
