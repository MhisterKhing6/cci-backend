package cci.confferenct.conference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cci.confferenct.conference.dto.request.ConferenceDetailsRequest;
import cci.confferenct.conference.dto.request.UpdateConfrenceRequest;
import cci.confferenct.conference.enums.RegistrationType;
import cci.confferenct.conference.model.Confrence;
import cci.confferenct.conference.model.ConfrenceRegistration;
import cci.confferenct.conference.model.User;
import cci.confferenct.conference.service.AdminServiceInterface;
import cci.confferenct.conference.service.TransactionServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;



@RestController
@RequestMapping("/api-admin")
@AllArgsConstructor
@Tag(name = "Admin", description = "Admin management APIs - Requires ADMIN role")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {
    private final  AdminServiceInterface adminService;
    private final TransactionServiceInterface transactionService;

    @GetMapping("/confrence/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get conference by ID", description = "Retrieve a specific conference details for admin")
    public Confrence getConferenceById(@Parameter(description = "Conference ID") @PathVariable String id) {
        return adminService.getConferenceById(id);
    }

    @PostMapping("/confrence")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create conference", description = "Add a new conference to the system")
    public Confrence addConfrence(@RequestBody @Valid ConferenceDetailsRequest conferenceDetails) {
        return adminService.addConference(conferenceDetails);
    }
    
    @DeleteMapping("confrence-delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete conference", description = "Remove a conference by ID")
    public void delete(@Parameter(description = "Conference ID") @PathVariable String id) {
        adminService.deleteConfrence(id);
    }


    @PutMapping("confrence/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update conference", description = "Update conference details")
    public Confrence update(
            @Parameter(description = "Conference ID") @PathVariable String id, 
            @RequestBody UpdateConfrenceRequest confrence) {
        return adminService.updateConference(id, confrence);
    }

    @PutMapping("/confrence/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle conference status", description = "Toggle conference active/inactive status")
    public cci.confferenct.conference.dto.response.UserResponse toggleConferenceStatus(
            @Parameter(description = "Conference ID") @PathVariable String id) {
        return adminService.toggleConferenceStatus(id);
    }

    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Search users", description = "Get paginated list of users with optional filters")
    public Page<User> searchUsers(
        @Parameter(description = "Filter by name") @RequestParam(required = false) String name,
        @Parameter(description = "Filter by email") @RequestParam(required = false) String email,
        @Parameter(description = "Filter by WhatsApp number") @RequestParam(required = false) String whatsappNumber,
        @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
        Pageable pageable) {
        return adminService.getAllUsers(name, email, whatsappNumber, pageable);
    }


    @GetMapping("/transactions/by-user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get transactions by user email", description = "Retrieve transactions for a specific user by email")
    public Page<cci.confferenct.conference.model.Transaction> getTransactionsByUserEmail(
            @Parameter(description = "User email") @PathVariable String email,
            @PageableDefault(size = 10, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return transactionService.getUserTransactions(email, pageable);
    }

    @GetMapping("/transactions-all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all transactions", description = "Retrieve all transactions in the system")
    public Page<cci.confferenct.conference.model.Transaction> getAllTransactions(
            @PageableDefault(size = 10, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return transactionService.getAllTransactions(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/confrence-regitration")
    public Page<ConfrenceRegistration> search(
            @RequestParam(required = false) String conferenceId,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) RegistrationType registrationType,
            @PageableDefault(size = 50, sort = "registrationDate", direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return transactionService.adminConfrenceSearch(conferenceId, userEmail, registrationType, pageable
        );
    }
}
    

    
    

