package cci.confferenct.conference.dto.response;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
   private String message;
   private Map<String, String> details;
}
