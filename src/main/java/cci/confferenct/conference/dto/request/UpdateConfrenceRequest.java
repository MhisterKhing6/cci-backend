package cci.confferenct.conference.dto.request;

import lombok.Data;

@Data
public class UpdateConfrenceRequest {
    private String name;
    private String description;
    private long startDate;
    private long endDate;
    private String location;
    private Double costOfConfrence;
    private Boolean isActive;
}