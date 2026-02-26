package cci.confferenct.conference.service.imp;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cci.confferenct.conference.dto.request.ResetPassword;
import cci.confferenct.conference.dto.request.UpdateProfileRequest;
import cci.confferenct.conference.dto.request.UserLoginRequest;
import cci.confferenct.conference.dto.request.UserRegistrationRequest;
import cci.confferenct.conference.dto.request.VerificationRequest;
import cci.confferenct.conference.dto.request.verifyIdentityRequest;
import cci.confferenct.conference.dto.response.UserLoginResponse;
import cci.confferenct.conference.dto.response.UserResponse;
import cci.confferenct.conference.enums.UserRole;
import cci.confferenct.conference.exceptions.EntityNotFound;
import cci.confferenct.conference.exceptions.UserAlreadyExist;
import cci.confferenct.conference.exceptions.WrongCredentials;
import cci.confferenct.conference.model.Confrence;
import cci.confferenct.conference.model.User;
import cci.confferenct.conference.model.VerificationToken;
import cci.confferenct.conference.repository.ConfrenceRepository;
import cci.confferenct.conference.repository.UserRepository;
import cci.confferenct.conference.repository.VerificationRepository;
import cci.confferenct.conference.security.JWTConfig;
import cci.confferenct.conference.service.EmailNotificatonInterface;
import cci.confferenct.conference.service.UserServiceInterface;
import cci.confferenct.conference.util.VerificationCodeGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class UserService implements UserServiceInterface {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTConfig jwtConfig;
    private final VerificationRepository verificationTokenRepository;
    private final ConfrenceRepository confrenceRepository;
    private final EmailNotificatonInterface emailNotification;
    
    @Override
    public UserResponse register(UserRegistrationRequest request) {        
        User alreadyExisting  = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (alreadyExisting != null) {
            throw new UserAlreadyExist("User with email " + request.getEmail() + " already exists");
        }
        User user = registerUser(request, UserRole.USER, passwordEncoder);
        UserResponse userResponse = UserResponse.builder()
                .details(Map.of("email", user.getEmail(), "name", user.getName()))
                .message("Kindly check your email for verification code")
                .build();
        log.info("User registered successfully: {}", user.getEmail());
        String vcode = VerificationCodeGenerator.generate6DigitCode();
        VerificationToken token = new VerificationToken();
        token.setUserEmail(user.getEmail());
        token.setVerificationCode(vcode);
        token.setCreationTime(System.currentTimeMillis());
        verificationTokenRepository.save(token);
        emailNotification.sendOtp(VerificationCodeGenerator.generate6DigitCode(), user.getEmail());
        userRepository.save(user);
        return userResponse;
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new WrongCredentials("Invalid email or password");
        }
        String token = jwtConfig.generateAccessToken(user);
        UserLoginResponse response = new UserLoginResponse();
        response.setUser(user);
        response.setToken(token);
        return response;
    }


    @Override
    public UserResponse requestVerification(VerificationRequest vRequest) {
        UserResponse response = UserResponse.builder().build();
        response.setMessage("If you have an account with us, an email will be sent, kindly check your email");
        User user = userRepository.findByEmail(vRequest.getEmail()).orElse(null);
        if (user != null) {
            String verificationCode = VerificationCodeGenerator.generate6DigitCode();
            VerificationToken token = new VerificationToken();
            token.setUserEmail(vRequest.getEmail());
            token.setVerificationCode(verificationCode);
            token.setCreationTime(System.currentTimeMillis());
            verificationTokenRepository.save(token);
            response.setDetails(Map.of("vId", token.getId()));
        }
        return response;
    }
    
    @Override
    public UserResponse verifyIdentity(verifyIdentityRequest vRequest) {
        VerificationToken token = verificationTokenRepository.findById(vRequest.getVId()).orElseThrow(() -> new WrongCredentials("Invalid verification ID"));
        if (System.currentTimeMillis() - token.getCreationTime() > 15 * 60 * 1000) {
            throw new WrongCredentials("Verification code has expired");
        }
        if (!token.getVerificationCode().equals(vRequest.getVPassword())) {
            throw new WrongCredentials("Invalid verification code");
        }
        token.setVerified(true);
        verificationTokenRepository.save(token);
        UserResponse response = UserResponse.builder()
                .message("Verification successful")
                .details(Map.of("vId", token.getId()))
                .build();
        return response;
    }

    @Override
    public UserResponse resetPassword(ResetPassword resetPassword) {
        VerificationToken token = verificationTokenRepository.findById(resetPassword.getVId()).orElseThrow(
            () -> new WrongCredentials("Invalid verification ID")
        );  
        User user = userRepository.findByEmail(token.getUserEmail()).orElseThrow(() -> new WrongCredentials("User not found"));
        user.setPasswordHash(passwordEncoder.encode(resetPassword.getNewPassword()));
        userRepository.save(user);
        verificationTokenRepository.delete(token);
        UserResponse response = UserResponse.builder().message("Password reset successful").build();
        return response;
    }
    
    @Override
    public Page<Confrence> getConfrence(Pageable pageable) {
        return confrenceRepository.findAll(pageable);
    }

    @Override
    public Confrence getConferenceById(String id) {
        return confrenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Conference not found"));
    }

    @Override
    public User me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFound("User not found"));
        }
        throw new WrongCredentials("User not authenticated or not found");
    }
    private User registerUser(UserRegistrationRequest request, UserRole userRole, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setWhatsappNumber(request.getWhatsappNumber());
        user.setSex(request.getSex());
        user.setAgeGroup(request.getAgeGroup());
        user.setCCIMember(request.isCCIMember());
        user.setUserRole(userRole);
        user.setBalance(BigDecimal.ZERO);
        user.setActivated(true);
        return user;
    }

    @Override
    public User updateUserProfileUser(UpdateProfileRequest updateProfileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFound("user not found"));
            User updatedUser = updateUser(updateProfileRequest, email);
            return userRepository.save(updatedUser);
        }
        throw new WrongCredentials("User not authenticated or not found");
     }

    @Override
    public User updateProfileAdmin(UpdateProfileRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFound("user not found"));
        User updatedUser = updateUser(request, email);
        return userRepository.save(updatedUser);
    }
    private User updateUser(UpdateProfileRequest userDetails, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new WrongCredentials("User not found"));
        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }
        if (userDetails.getWhatsappNumber() != null) {
            user.setWhatsappNumber(userDetails.getWhatsappNumber());
        }
        if (userDetails.getSex() != null) {
            user.setSex(userDetails.getSex());
        }
        if (userDetails.getAgeGroup() != null) {
            user.setAgeGroup(userDetails.getAgeGroup());
        }
        if(userDetails.getIsCCIMember() != null) {
            user.setCCIMember(userDetails.getIsCCIMember());
        }
        return user;
    }
}
