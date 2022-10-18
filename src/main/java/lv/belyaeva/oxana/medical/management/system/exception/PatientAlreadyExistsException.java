package lv.belyaeva.oxana.medical.management.system.exception;

public class PatientAlreadyExistsException extends RuntimeException {

    private String message;

    public PatientAlreadyExistsException() {
    }

    public PatientAlreadyExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
