package cci.confferenct.conference.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cci.confferenct.conference.dto.request.ConferenceDetailsRequest;
import cci.confferenct.conference.dto.request.UpdateConfrenceRequest;
import cci.confferenct.conference.dto.response.UserResponse;
import cci.confferenct.conference.model.Confrence;
import cci.confferenct.conference.model.User;


public interface AdminServiceInterface {
    Confrence addConference(ConferenceDetailsRequest conferenceDetails);

    UserResponse deleteConfrence(String confrenceId);

    Confrence updateConference(String id, UpdateConfrenceRequest updatedConference);

    Page<User> getAllUsers(String name, String email, String whatsappNumber, Pageable pageable);

    UserResponse changeActivateStatus(String userId, boolean activate);

    UserResponse deleteUser(String userId);

    Confrence getConferenceById(String id);

    UserResponse toggleConferenceStatus(String conferenceId);
    
    Page<Confrence> getConfrence(Pageable pageable);


}

