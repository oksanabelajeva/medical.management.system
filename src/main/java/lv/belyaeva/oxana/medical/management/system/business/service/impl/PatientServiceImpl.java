package lv.belyaeva.oxana.medical.management.system.business.service.impl;

import lombok.extern.log4j.Log4j2;
import lv.belyaeva.oxana.medical.management.system.business.mapper.PatientMapper;
import lv.belyaeva.oxana.medical.management.system.business.repository.PatientRepository;
import lv.belyaeva.oxana.medical.management.system.business.repository.model.PatientDAO;
import lv.belyaeva.oxana.medical.management.system.business.service.PatientService;
import lv.belyaeva.oxana.medical.management.system.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientMapper patientMapper;

    @Override
    public Patient savePatient(Patient patient) {
        if (!hasNoMatch(patient)) {
            log.error("Patient conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        PatientDAO patientSaved = patientRepository.save(patientMapper.patientToPatientDAO(patient));
        log.info("New patient saved: {}", () -> patientSaved);
        return patientMapper.patientDAOToPatient(patientSaved);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        PatientDAO patientSaved = patientRepository.save(patientMapper.patientToPatientDAO(patient));
        log.info("Patient data was updated: {}", () -> patientSaved);
        return patientMapper.patientDAOToPatient(patientSaved);
    }

    @Override
    public List<Patient> findAllPatients() {
        List<PatientDAO> patientDAOList = patientRepository.findAll();
        log.info("Get list of all patients. Size is: {}", patientDAOList::size);
        return patientDAOList.stream().map(patientMapper::patientDAOToPatient).collect(Collectors.toList());
    }

    @Override
    public Optional<Patient> findPatientById(Long patientId) {
        Optional<Patient> patientById = patientRepository.findById(patientId).flatMap(patient ->
                Optional.ofNullable(patientMapper.patientDAOToPatient(patient)));
        log.info("Patient with id {} is {}", patientId, patientById);
        return patientById;
    }

    @Override
    public void deletePatientById(Long patientId) {
        patientRepository.deleteById(patientId);
        log.info("Patient with id {} is deleted", patientId);
    }

    public boolean hasNoMatch(Patient patient) {
        return patientRepository.findAll().stream()
                .noneMatch(patientDAO -> patientDAO.getName().equals(patient.getName())
                        && patientDAO.getSurname().equals(patient.getSurname())
                        && patientDAO.getPersonalCode().equals(patient.getPersonalCode())
                        && patientDAO.getDateOfBirth().equals(patient.getDateOfBirth()));
    }
}
