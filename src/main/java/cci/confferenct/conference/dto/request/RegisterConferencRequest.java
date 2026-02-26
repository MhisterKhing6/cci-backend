package cci.confferenct.conference.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterConferencRequest {
    @NotBlank(message="ConfrenceId is required")
    private String conferenceId;
    
}
