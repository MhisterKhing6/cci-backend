package cci.confferenct.conference.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cci.confferenct.conference.dto.request.RegisterConferencRequest;
import cci.confferenct.conference.enums.RegistrationType;
import cci.confferenct.conference.model.ConfrenceRegistration;
import cci.confferenct.conference.model.Transaction;

public interface  TransactionServiceInterface {
    
    Page<Transaction> getAllTransactions(Pageable pageable);
    
    Page<Transaction> getUserTransactions(String userId, Pageable pageable);

    ConfrenceRegistration registerConference(RegisterConferencRequest registerConferencRequest, String email);

    Page<ConfrenceRegistration> adminConfrenceSearch(String conferenceId, String userEmail, RegistrationType registrationType,Pageable pageable);
    
    Page<ConfrenceRegistration> getUserRegistrations( String userEmail, Pageable pageable);
}
