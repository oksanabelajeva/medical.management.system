package lv.belyaeva.oxana.medical.management.system.business.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lv.belyaeva.oxana.medical.management.system.model.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
public class PatientDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", unique = true, nullable = false)
    private Long patientId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "age")
    private Long age;

    @Column(name = "personal_code")
    private String personalCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "residing_address")
    private String residingAddress;

    @Column(name = "get_to_hospital_date")
    private String getToHospitalDate;

    @Column(name = "leave_hospital_date")
    private String leaveHospitalDate;

    @Column(name = "disease_information")
    private String diseaseInformation;

    @Column(name = "consumed_medicines")
    private String consumedMedicines;
}
