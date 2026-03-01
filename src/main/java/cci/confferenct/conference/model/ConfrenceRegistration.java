package cci.confferenct.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import cci.confferenct.conference.enums.RegistrationType;
import lombok.Data;

@Data
public class ConfrenceRegistration {
    @Id
    private String id;
    @Indexed
    private String confrenceId;
    @Indexed
    private String userEmail;
    private long registrationDate;
    private RegistrationType registrationType;
    private double cost;
    private UserInfo userInfo;
    private ConfrenceInfo confrenceInfo; 
    private int numberOfChildren; 
}
