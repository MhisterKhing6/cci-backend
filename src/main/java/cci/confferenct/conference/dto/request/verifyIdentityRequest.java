package cci.confferenct.conference.dto.request;

import lombok.Data;

@Data
public class verifyIdentityRequest {
  
    private final String vId;
    private final String vPassword;
}
