package lv.belyaeva.oxana.medical.management.system.business.repository;

import lv.belyaeva.oxana.medical.management.system.business.repository.model.PatientDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientDAO, Long> {
}
