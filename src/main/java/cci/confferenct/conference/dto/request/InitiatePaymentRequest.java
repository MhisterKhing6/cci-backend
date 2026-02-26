package cci.confferenct.conference.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class InitiatePaymentRequest {
    private String email;
    private BigDecimal amount;
    private String reference;
    private String callBackUrl;
}
