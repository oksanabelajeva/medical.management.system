package lv.belyaeva.oxana.medical.management.system.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lv.belyaeva.oxana.medical.management.system.business.mapper.PatientMapper;
import lv.belyaeva.oxana.medical.management.system.business.repository.PatientRepository;
import lv.belyaeva.oxana.medical.management.system.business.repository.model.PatientDAO;
import lv.belyaeva.oxana.medical.management.system.business.service.PatientService;
import lv.belyaeva.oxana.medical.management.system.exception.NoSuchPatientExistsException;
import lv.belyaeva.oxana.medical.management.system.exception.PatientAlreadyExistsException;
import lv.belyaeva.oxana.medical.management.system.model.Gender;
import lv.belyaeva.oxana.medical.management.system.model.Patient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    @CacheEvict(cacheNames = "patientsList", allEntries = true)
    @Override
    public Patient savePatient(Patient patient) throws Exception {
        if (!hasNoMatch(patient)) {
            throw new PatientAlreadyExistsException("The patient already exists!");
        }
        checkIfCurrentDateIsBeforeDateOfBirth(LocalDate.parse(patient.getDateOfBirth()), LocalDate.now());
        patient.setAge((long) calculateAgeOfThePatient(LocalDate.parse(patient.getDateOfBirth()),
                LocalDate.now()));
        PatientDAO patientSaved = patientRepository.save(patientMapper.patientToPatientDAO(patient));
        log.info("New patient saved: {}", () -> patientSaved);
        return patientMapper.patientDAOToPatient(patientSaved);
    }

    @CacheEvict(cacheNames = "patientsList", allEntries = true)
    @Override
    public Patient updatePatient(Patient patient) throws Exception {

        PatientDAO existingPatient = patientRepository.findById(patient.getPatientId()).orElse(null);
        if (existingPatient == null) {
            throw new NoSuchPatientExistsException("No such patient exists!");
        } else {
            checkIfCurrentDateIsBeforeDateOfBirth(LocalDate.parse(patient.getDateOfBirth()), LocalDate.now());
            patient.setAge((long) calculateAgeOfThePatient(LocalDate.parse(patient.getDateOfBirth()),
                    LocalDate.now()));
            PatientDAO patientSaved = patientRepository.save(patientMapper.patientToPatientDAO(patient));
            log.info("Patient data was updated: {}", () -> patientSaved);
            return patientMapper.patientDAOToPatient(patientSaved);
        }
    }

    @Cacheable(value = "patientsList")
    @Scheduled(fixedDelay = 300000)
    @Override
    public List<Patient> findAllPatients() {
        List<PatientDAO> patientDAOList = patientRepository.findAll();
        log.info("Get list of all patients. Size is: {}", patientDAOList::size);
        return patientDAOList.stream().map(patientMapper::patientDAOToPatient).collect(Collectors.toList());
    }

    @Override
    public Optional<Patient> findPatientById(Long patientId) {
        Optional<Patient> patientById = Optional.ofNullable(patientRepository.findById(patientId).flatMap(patient ->
                Optional.ofNullable(patientMapper.patientDAOToPatient(patient))).orElseThrow(() -> new NoSuchElementException(
                "No patient exists in database with id " + patientId + ".")));
        log.info("Patient with id {} is {}", patientId, patientById);
        return patientById;
    }


    @Override
    public List<Patient> findAllPatientsByGender(Gender gender) {
        List<PatientDAO> patientDAOListSortedByGender = patientRepository.findAllPatientsByGender(gender);
        return patientDAOListSortedByGender.stream().map(patientMapper::patientDAOToPatient).collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "patientsList", allEntries = true)
    @Override
    public void deletePatientById(Long patientId) {
        patientRepository.deleteById(patientId);
        log.info("Patient with id {} is deleted", patientId);
    }

    public boolean hasNoMatch(Patient patient) {
        return patientRepository.findAll().stream()
                .noneMatch(patientDAO -> patientDAO.getName().equalsIgnoreCase(patient.getName())
                        && patientDAO.getSurname().equalsIgnoreCase(patient.getSurname())
                        && patientDAO.getPersonalCode().equals(patient.getPersonalCode())
                        && patientDAO.getDateOfBirth().equals(patient.getDateOfBirth()));
    }

    static void checkIfCurrentDateIsBeforeDateOfBirth(LocalDate dateOfBirth, LocalDate currentDate) throws Exception {
        if (currentDate.isBefore(dateOfBirth)) {
            throw new Exception("Please insert valid date of birth, the patient is not born yet.");
        }
    }

    public int calculateAgeOfThePatient(LocalDate dateOfBirth, LocalDate currentDate) {
        Period calculateAgeOfThePatient = Period.between(dateOfBirth, currentDate);
        return calculateAgeOfThePatient.getYears();
    }
}
