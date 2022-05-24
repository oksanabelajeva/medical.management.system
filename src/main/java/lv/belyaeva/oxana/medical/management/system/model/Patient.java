package lv.belyaeva.oxana.medical.management.system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.belyaeva.oxana.medical.management.system.swagger.DescriptionVariables;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(description = "Model of patient")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @ApiModelProperty(notes = "Autogenerated unique id of the patient")
    private Long patientId;

    @ApiModelProperty(notes = "Name of the patient")
    @NotNull
    @NotEmpty
    private String name;

    @ApiModelProperty(notes = "Surname of the patient")
    @NotNull
    @NotEmpty
    private String surname;

    @ApiModelProperty(notes = "Gender of the patient")
    @NotNull
    private Gender gender;

    @ApiModelProperty(notes = "Date of birth of the patient")
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonFormat(pattern = DescriptionVariables.DATE_FORMAT_PATTERN)
    private String dateOfBirth;

    @ApiModelProperty(notes = "Patient's personal code")
    @NotNull
    @NotEmpty
    private String personalCode;

    @ApiModelProperty(notes = "Patient's phone number")
    @NotNull
    @NotEmpty
    private String phoneNumber;

    @ApiModelProperty(notes = "Patient's residing address")
    @NotNull
    @NotEmpty
    private String residingAddress;

    @ApiModelProperty(notes = "When the patient got to the hospital")
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonFormat(pattern = DescriptionVariables.DATE_FORMAT_PATTERN)
    private String getToHospitalDate;

    @ApiModelProperty(notes = "When the patient left the hospital")
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonFormat(pattern = DescriptionVariables.DATE_FORMAT_PATTERN)
    private String leaveHospitalDate;

    @ApiModelProperty(notes = "Information about patient's disease")
    @NotNull
    @NotEmpty
    private String diseaseInformation;

    @ApiModelProperty(notes = "Information about consumed medicines")
    @NotNull
    @NotEmpty
    private String consumedMedicines;
}
