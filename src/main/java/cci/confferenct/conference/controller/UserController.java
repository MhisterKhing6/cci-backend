package cci.confferenct.conference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cci.confferenct.conference.dto.request.ResetPassword;
import cci.confferenct.conference.dto.request.UserLoginRequest;
import cci.confferenct.conference.dto.request.UserRegistrationRequest;
import cci.confferenct.conference.dto.request.VerificationRequest;
import cci.confferenct.conference.dto.request.verifyIdentityRequest;
import cci.confferenct.conference.dto.response.UserLoginResponse;
import cci.confferenct.conference.dto.response.UserResponse;
import cci.confferenct.conference.model.Confrence;
import cci.confferenct.conference.service.PaymentServiceInterface;
import cci.confferenct.conference.service.UserServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;



@RestController
@RequestMapping("/api-user")
@AllArgsConstructor
@Tag(name = "User", description = "Public user APIs for registration, authentication, and conference browsing")
public class UserController {
    private final UserServiceInterface userService;
    private final PaymentServiceInterface paymentService;
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the API is running")
    public String health() {
        return "ok";
    }
    
    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Create a new user account")
    public UserResponse register(@RequestBody @Valid UserRegistrationRequest userDetails) {
        return userService.register(userDetails);
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and receive JWT token")
    public UserLoginResponse login(@RequestBody @Valid UserLoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
    
    @PostMapping("/request-forget-password")
    @Operation(summary = "Request password reset", description = "Send verification code to user's email for password reset")
    public UserResponse requestForgetPwd(@RequestBody @Valid VerificationRequest verificationRequest) {
        return userService.requestVerification(verificationRequest);
    }
    
    @PostMapping("/verify-identity")
    @Operation(summary = "Verify identity", description = "Verify user identity using verification code")
    public UserResponse verifyIdentity(@RequestBody @Valid verifyIdentityRequest verifyIdentityRequest) {
        return userService.verifyIdentity(verifyIdentityRequest);
    }
    
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset user password after verification")
    public UserResponse resetPassword(@RequestBody @Valid ResetPassword resetPassword) {
        return userService.resetPassword(resetPassword);
    }
    
    @GetMapping("/confrences")
    @Operation(summary = "List conferences", description = "Get paginated list of all conferences")
    public Page<Confrence> getAll(
        @PageableDefault(sort = "startDate", direction = Sort.Direction.DESC, size = 30) Pageable pageable) {
        return userService.getConfrence(pageable);
    }

    @GetMapping("confrence/{id}")
    @Operation(summary = "Get conference", description = "Retrieve conference details by ID")
    public Confrence getId(@Parameter(description = "Conference ID") @PathVariable String id) {
        return userService.getConferenceById(id);
    }

    @GetMapping("/payment/verify/{reference}")
    @Operation(summary = "Verify payment", description = "Verify a payment transaction by reference")
    public ResponseEntity<UserResponse> verify(@PathVariable String reference) {

    UserResponse response = paymentService
            .verifyPayment(reference)
            .block();

    return ResponseEntity.ok(response);
}
    
}
