package lv.belyaeva.oxana.medical.management.system.business.repository;

import lv.belyaeva.oxana.medical.management.system.business.repository.model.PatientDAO;
import lv.belyaeva.oxana.medical.management.system.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientDAO, Long> {

    List<PatientDAO> findAllPatientsByGender(Gender gender);
}
