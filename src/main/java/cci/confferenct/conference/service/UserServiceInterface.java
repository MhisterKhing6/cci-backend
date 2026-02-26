package cci.confferenct.conference.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cci.confferenct.conference.dto.request.ResetPassword;
import cci.confferenct.conference.dto.request.UpdateProfileRequest;
import cci.confferenct.conference.dto.request.UserLoginRequest;
import cci.confferenct.conference.dto.request.UserRegistrationRequest;
import cci.confferenct.conference.dto.request.VerificationRequest;
import cci.confferenct.conference.dto.request.verifyIdentityRequest;
import cci.confferenct.conference.dto.response.UserLoginResponse;
import cci.confferenct.conference.dto.response.UserResponse;
import cci.confferenct.conference.model.Confrence;
import cci.confferenct.conference.model.User;

public interface UserServiceInterface {
    
    UserResponse register(UserRegistrationRequest request);

    UserLoginResponse login(UserLoginRequest request);

    UserResponse requestVerification(VerificationRequest vRequest);

    UserResponse verifyIdentity(verifyIdentityRequest vRequest);

    UserResponse resetPassword(ResetPassword resetPassword);

    Page<Confrence> getConfrence(Pageable pageable);

    Confrence getConferenceById(String id);
    
    User me();

    User updateUserProfileUser(UpdateProfileRequest updateProfileRequest);
    
    User updateProfileAdmin(UpdateProfileRequest request, String email);

}

