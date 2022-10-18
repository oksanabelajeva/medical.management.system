package lv.belyaeva.oxana.medical.management.system.exception;

public class NoSuchPatientExistsException extends RuntimeException {

    private String message;

    public NoSuchPatientExistsException() {
    }

    public NoSuchPatientExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
