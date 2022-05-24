package lv.belyaeva.oxana.medical.management.system.business.mapper;

import lv.belyaeva.oxana.medical.management.system.business.repository.model.PatientDAO;
import lv.belyaeva.oxana.medical.management.system.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDAO patientToPatientDAO(Patient patient);

    Patient patientDAOToPatient(PatientDAO patientDAO);
}
