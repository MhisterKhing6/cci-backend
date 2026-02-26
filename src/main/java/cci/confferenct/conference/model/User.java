package cci.confferenct.conference.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cci.confferenct.conference.enums.SexType;
import cci.confferenct.conference.enums.UserRole;
import lombok.Data;

@Data
@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    @Indexed
    private String name;

    @JsonIgnore
    private String passwordHash;
    
    @Indexed
    private String whatsappNumber;
    private SexType sex;
    private String ageGroup;
    private boolean isCCIMember;
    @Indexed
    private String email;
    private UserRole userRole;
    private BigDecimal balance;
    private boolean isActivated;
     
}
