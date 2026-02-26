package cci.confferenct.conference.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class VerificationToken {
    @Id
    private String id;

    private String userEmail;

    private String verificationCode;

    private long creationTime;

    private boolean isVerified;

}
