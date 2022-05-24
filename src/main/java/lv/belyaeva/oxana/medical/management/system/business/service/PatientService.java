package lv.belyaeva.oxana.medical.management.system.business.service;

import lv.belyaeva.oxana.medical.management.system.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    Patient savePatient(Patient patient);

    Patient updatePatient(Patient patient);

    List<Patient> findAllPatients();

    Optional<Patient> findPatientById(Long patientId);

    void deletePatientById(Long patientId);
}
