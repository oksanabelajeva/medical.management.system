package lv.belyaeva.oxana.medical.management.system.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lv.belyaeva.oxana.medical.management.system.business.service.PatientService;
import lv.belyaeva.oxana.medical.management.system.config.rules.DroolsConfig;
import lv.belyaeva.oxana.medical.management.system.model.Gender;
import lv.belyaeva.oxana.medical.management.system.model.Patient;
import lv.belyaeva.oxana.medical.management.system.config.swagger.DescriptionVariables;
import lv.belyaeva.oxana.medical.management.system.config.swagger.HTMLResponseMessages;
import org.kie.api.builder.KieBuilder;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(tags = {DescriptionVariables.PATIENT})
@Log4j2
@RestController
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    private final KieSession kieSession;

    @PostMapping
    @ApiOperation(value = "Saves the patient in database.",
            notes = "If provided valid patient's data, saves it",
            response = Patient.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Patient> postPatient(@Valid @RequestBody Patient patient,
                                               BindingResult bindingResult) throws Exception {
        log.info("Create new patient by passing : {}", patient);
        if (bindingResult.hasErrors()) {
            log.error("Patient have an error: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Patient patientSaved = patientService.savePatient(patient);
        kieSession.insert(patientSaved);
        kieSession.fireAllRules();
        Patient patientUpdated = patientService.updatePatient(patientSaved);
        log.info("New patient is created: {}", patientUpdated);
        return new ResponseEntity<>(patientUpdated, HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    @ApiOperation(value = "Updates patient's record by id.",
            notes = "Updates patient's record if id exists.",
            response = Patient.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Patient> updatePatientById(
            @ApiParam(value = "id of the patient", required = true)
            @PathVariable @NonNull Long patientId,
            @Valid @RequestBody Patient updatedPatient, BindingResult bindingResult) throws Exception {
        log.info("Update existing patient with id: {} and new body: {}", patientId, updatedPatient);
        if (bindingResult.hasErrors() || !patientId.equals(updatedPatient.getPatientId())) {
            log.warn("Patient for update with id {} not found", patientId);
            return ResponseEntity.notFound().build();
        }
        patientService.updatePatient(updatedPatient);
        kieSession.insert(updatedPatient);
        kieSession.fireAllRules();
        log.debug("Patient with id {} is updated: {}", patientId, updatedPatient);
        return new ResponseEntity<>(updatedPatient, HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Finds all patients.",
            notes = "Returns the entire list of patients",
            response = Patient.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200, response = Patient.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<Patient>> findAllPatients() {
        log.info("Retrieve list of patients");
        List<Patient> patientList = patientService.findAllPatients();
        if (patientList.isEmpty()) {
            log.warn("Patients list is empty! {}", patientList);
            return ResponseEntity.notFound().build();
        }
        log.debug("Patients list is found. Size: {}", patientList::size);
        return ResponseEntity.ok(patientList);
    }

    @GetMapping("/{patientId}")
    @ApiOperation(value = "Finds the patient's record by id.",
            notes = "Provide an id to search specific patient's record in database.",
            response = Patient.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<Patient> findPatientById(
            @ApiParam(value = "id of the patient's record", allowableValues = DescriptionVariables.MAX_LONG_RANGE,
                    required = true)
            @NonNull @PathVariable Long patientId) {
        log.info("Find patient's record by passing id of the patient's record" +
                ", where patient's record id is: {}.", patientId);
        Optional<Patient> patient = (patientService.findPatientById(patientId));
        if (!patient.isPresent()) {
            log.warn("Patient's record with id {} is not found.", patientId);
        } else {
            log.debug("Patient's record with id {} is found: {}.", patientId, patient);
        }
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/filteredByGenderList/{gender}")
    @ApiOperation(
            value = "Finds the patient's records filtered by gender.",
            notes = "Provide a gender to search specific patients' records in database.",
            response = Patient.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<Patient>> getToDoTasksByPriority(
            @ApiParam(value = "Gender", required = true)
            @PathVariable("gender") Gender gender) {
        List<Patient> patientListByGender = patientService.findAllPatientsByGender(gender);
        if (patientListByGender.isEmpty()) {
            log.info("Any patient with gender {} is not found. List is empty", gender);
            return ResponseEntity.notFound().build();
        }
        log.info("Patients with gender {} are found. Size = {}", gender, patientListByGender.size());
        return ResponseEntity.ok().body(patientListByGender);
    }

    @DeleteMapping("/{patientId}")
    @ApiOperation(value = "Deletes the patient's record by id.",
            notes = "Deletes the patient's record if provided id exists.",
            response = Patient.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITHOUT_DATA),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePatientById(
            @ApiParam(value = "The id of the patient", allowableValues = DescriptionVariables.MAX_LONG_RANGE,
                    required = true)
            @NonNull @PathVariable Long patientId) {
        log.info("Delete patient's record by passing id, where id is: {}", patientId);
        Optional<Patient> patient = patientService.findPatientById(patientId);
        if (!(patient.isPresent())) {
            log.warn("Patient for delete with id {} is not found.", patientId);
            return ResponseEntity.notFound().build();
        }
        patientService.deletePatientById(patientId);
        log.debug("Patient with id {} is deleted: {}", patientId, patient);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
