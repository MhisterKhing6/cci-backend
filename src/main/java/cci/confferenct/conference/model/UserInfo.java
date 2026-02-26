package cci.confferenct.conference.model;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private String name;
    private String userId;
    private String email;
    private String whatsappNumber;
}
