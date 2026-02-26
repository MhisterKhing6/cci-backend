package cci.confferenct.conference.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cci.confferenct.conference.dto.request.InitiatePaymentRequest;
import cci.confferenct.conference.service.PaymentServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment processing APIs using Paystack")
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {

    private final PaymentServiceInterface paymentService;

    @PostMapping("/initiate")
    @Operation(summary = "Initiate payment", description = "Initialize a payment transaction with Paystack")
    public ResponseEntity<?> initiatePayment(@RequestBody InitiatePaymentRequest request) {
        Map response = paymentService.initiatePayment(request).block();
        return ResponseEntity.ok(response);
    }

    
}