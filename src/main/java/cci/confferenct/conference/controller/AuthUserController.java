package cci.confferenct.conference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cci.confferenct.conference.dto.request.RegisterConferencRequest;
import cci.confferenct.conference.model.ConfrenceRegistration;
import cci.confferenct.conference.model.Transaction;
import cci.confferenct.conference.model.User;
import cci.confferenct.conference.service.TransactionServiceInterface;
import cci.confferenct.conference.service.UserServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api-auth")
@Slf4j
@AllArgsConstructor
@Tag(name = "Authentication", description = "Authenticated user APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthUserController {
    private final UserServiceInterface userService;
    private final TransactionServiceInterface transactionService;

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Retrieve authenticated user's profile information")
    public User me() {
        return userService.me();
    }
    
    @GetMapping("/user-transactions")
    @Operation(summary = "Get user's transactions", description = "Retrieve authenticated user's transaction history")
    public Page<Transaction> getMyTransactions( @PageableDefault( size = 10, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable,Authentication authentication) {
    String email = authentication.getName();
    log.info("Fetching transactions for user: {}", email);
    return transactionService.getUserTransactions(email, pageable);
    }

    @PostMapping("/register-confrence")
    @Operation(summary = "Register for conference", description = "Register authenticated user for a conference")
    public ConfrenceRegistration registerConfrence(@RequestBody @Valid RegisterConferencRequest registerConferencRequest, Authentication authentication) {
    String email = authentication.getName();
    log.info("Fetching transactions for user: {}", email);
    return transactionService.registerConference(registerConferencRequest, email);
    }

    @GetMapping("confrence-regitraion")
    public Page<ConfrenceRegistration> getMyRegistrations( @PageableDefault( size = 50, sort = "registrationDate", direction = Sort.Direction.DESC ) Pageable pageable,Authentication authentication) {

        String email = authentication.getName();

        return transactionService.getUserRegistrations(email, pageable);
    }

}
