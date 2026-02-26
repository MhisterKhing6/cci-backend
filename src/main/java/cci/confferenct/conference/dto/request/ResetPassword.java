package cci.confferenct.conference.dto.request;

import lombok.Data;

@Data
public class ResetPassword {
  private String vId;
  private String newPassword;  
}
