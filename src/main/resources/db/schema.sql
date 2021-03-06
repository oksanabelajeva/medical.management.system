DROP TABLE IF EXISTS PATIENT;

CREATE TABLE PATIENT(
PATIENT_ID BIGINT(20) NOT NULL AUTO_INCREMENT,
NAME VARCHAR(255) NOT NULL,
SURNAME VARCHAR(255) NOT NULL,
GENDER VARCHAR(255) NOT NULL,
DATE_OF_BIRTH VARCHAR(255) NOT NULL,
AGE BIGINT(20) NOT NULL,
PERSONAL_CODE VARCHAR(255) NOT NULL,
PHONE_NUMBER VARCHAR(255) NOT NULL,
RESIDING_ADDRESS VARCHAR(255) NOT NULL,
GET_TO_HOSPITAL_DATE VARCHAR(255) NOT NULL,
LEAVE_HOSPITAL_DATE VARCHAR(255) NOT NULL,
DISEASE_INFORMATION VARCHAR(255) NOT NULL,
CONSUMED_MEDICINES VARCHAR(255) NOT NULL,
WARNING_INFORMATION VARCHAR(255),
PRIMARY KEY(PATIENT_ID));