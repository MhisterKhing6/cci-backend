package cci.confferenct.conference.service.imp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cci.confferenct.conference.dto.request.InitiatePaymentRequest;
import cci.confferenct.conference.dto.response.UserResponse;
import cci.confferenct.conference.enums.TransactionTypes;
import cci.confferenct.conference.exceptions.EntityNotFound;
import cci.confferenct.conference.exceptions.WrongCredentials;
import cci.confferenct.conference.model.Transaction;
import cci.confferenct.conference.model.User;
import cci.confferenct.conference.model.UserInfo;
import cci.confferenct.conference.repository.TransactionRepository;
import cci.confferenct.conference.repository.UserRepository;
import cci.confferenct.conference.service.PaymentServiceInterface;
import cci.confferenct.conference.util.PaystackReferenceGenerator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaystackService implements PaymentServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(PaystackService.class);

    private final WebClient paystackWebClient;
    private final TransactionRepository transactionRepository;
    @Value("${paystack.base.callbackUrl}")
    private String callbackUrl;
        private final UserRepository userRepository;

    private Mono<Map> initializeTransaction( String email, BigDecimal amount, String reference) {

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);

        long amountInPesewas = amount
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        body.put("amount", amountInPesewas);
        body.put("reference", reference);
        body.put("currency", "GHS");
        body.put("callback_url", callbackUrl);

        log.info("Initializing Paystack transaction for email: {}, amount: {}", email, amountInPesewas);

        return paystackWebClient.post()
                .uri("/transaction/initialize")
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> response.bodyToMono(String.class)
                        .doOnNext(errorBody -> log.error("Paystack API error response: {}", errorBody))
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Paystack API error: " + errorBody))))
                .bodyToMono(Map.class);
    }

    @Override
    public Mono<Map> initiatePayment(InitiatePaymentRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null ||
        !(authentication.getPrincipal() instanceof UserDetails)) {
        throw new WrongCredentials("User not authenticated");
    }

    String email =
            ((UserDetails) authentication.getPrincipal()).getUsername();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFound("User not found"));

    String reference = PaystackReferenceGenerator.generateReference();

    return initializeTransaction(
            user.getEmail(),
            request.getAmount(),
            reference
    ).flatMap(response -> {

        Map data = (Map) response.get("data");

        String accessCode = (String) data.get("access_code");
        String returnedReference = (String) data.get("reference");

        Transaction transaction = new Transaction();
        transaction.setReference(returnedReference);
        transaction.setAccessCode(accessCode);
        transaction.setStatus("PENDING");
        transaction.setTransactionDate(System.currentTimeMillis());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionTypes.TOPUP);
        UserInfo userInfo = UserInfo.builder().
                                     userId(user.getId()).
                                     email(user.getEmail())
                                     .name(user.getName())
                                     .whatsappNumber(user.getWhatsappNumber())
                                     .build();

        transaction.setUserInfo(userInfo);

        transactionRepository.save(transaction);

        return Mono.just(response);
        });
    } 


    @Override
    public Mono<UserResponse> verifyPayment(String reference) {

    return paystackWebClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/transaction/verify/{reference}")
                    .build(reference))
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                        .flatMap(errorBody ->
                                Mono.error(new RuntimeException("Paystack API error: " + errorBody)))
            )
            .bodyToMono(Map.class)
            .map(response -> {

                Map data = (Map) response.get("data");
                String paymentStatus = (String) data.get("status");

                Transaction transaction = transactionRepository.findByReference(reference)
                        .orElseThrow(() -> new EntityNotFound("Transaction not found"));

                if ("success".equalsIgnoreCase(paymentStatus)) {

                    User user = userRepository
                            .findByEmail(transaction.getUserInfo().getEmail())
                            .orElseThrow(() -> new EntityNotFound("User not found"));

                    user.setBalance(user.getBalance().add(transaction.getAmount()));
                    userRepository.save(user);
                }

                transaction.setStatus(paymentStatus);
                transactionRepository.save(transaction);

                return UserResponse.builder()
                        .message("Payment verification completed")
                        .details(Map.of("status", paymentStatus))
                        .build();
            });
}
}