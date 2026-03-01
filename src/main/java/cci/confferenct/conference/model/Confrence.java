package cci.confferenct.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Document(collection = "conferences")
@Data
public class Confrence {
    @Id
    private String id;
    private String name;
    private String description;
    private long startDate;
    private long endDate;
    private String location;
    private double costOfConfrence;
    private boolean isActive;

}
