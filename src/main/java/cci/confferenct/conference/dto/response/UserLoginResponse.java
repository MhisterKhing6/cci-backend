package cci.confferenct.conference.dto.response;

import cci.confferenct.conference.model.User;
import lombok.Data;

@Data
public class UserLoginResponse {
    private User user;
    private String token;
}
