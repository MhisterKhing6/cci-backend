package cci.confferenct.conference.dto.request;

import cci.confferenct.conference.enums.SexType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    @NotBlank(message = "Name is required")
    private String name;
   
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String password;

   @Pattern(regexp = "^\\+?[0-9]\\d{7,14}$", message = "Invalid WhatsApp number format")
    private String whatsappNumber;

    private boolean isCCIMember;

    @NotNull(message = "sex  is required")
    private SexType sex;

    @NotBlank(message = "age group is required")
    private String ageGroup;
}
