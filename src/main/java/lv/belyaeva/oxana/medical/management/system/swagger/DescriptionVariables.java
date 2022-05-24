package lv.belyaeva.oxana.medical.management.system.swagger;

public class DescriptionVariables {

    // Key strings for Swagger tags. Used to register controller descriptions
    public static final String PATIENT = "Patient Controller";

    // Variables for controller descriptions
    public static final String NON_NULL_MAX_LONG = "Required non-null value. Starting from 1 to Long.MAX_VALUE";
    public static final String MAX_LONG_RANGE = "range[1, 9223372036854775807]";

    // Variables for model descriptions
    public static final String MODEL_ID_MIN = "Id must be bigger than 0";
    public static final String MODEL_ID_MAX = "Id must be less than 9,223,372,036,854,775,808";
    public static final String CONTAIN_LETTERS_MESSAGE = "Type must contain only letters";
    public static final String CONTAIN_ALPHANUMERIC_CHAR_MESSAGE = "This field may contain only letters, numbers and spaces";
    public static final String STRING_PATTERN = "^[a-zA-Z0-9\\s]+$";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
}
