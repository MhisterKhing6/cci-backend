package cci.confferenct.conference.dto.request;


import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConferenceDetailsRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "CCI price must be greater than 0")
    private double cciPrice;

    @DecimalMin(value = "0.0", inclusive = false, message = "Non-CCI price must be greater than 0")
    private double nonCciPrice;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @NotNull(message = "Location is required")
    private String location;
}