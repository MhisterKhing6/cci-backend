package cci.confferenct.conference.service;

import java.util.Map;

import cci.confferenct.conference.dto.request.InitiatePaymentRequest;
import cci.confferenct.conference.dto.response.UserResponse;
import reactor.core.publisher.Mono;

public interface  PaymentServiceInterface {
    
    Mono<Map> initiatePayment(InitiatePaymentRequest request);

    Mono<UserResponse> verifyPayment(String reference) ;
}
