package cci.confferenct.conference.dto.request;


import cci.confferenct.conference.enums.SexType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String whatsappNumber;

    private String ageGroup;

    private Boolean isCCIMember;

    private SexType sex;
}