package cci.confferenct.conference.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cci.confferenct.conference.model.VerificationToken;

public interface VerificationRepository extends MongoRepository<VerificationToken, String> {
    VerificationToken findByUserEmail(String email);    
    
}
