package cci.confferenct.conference.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import cci.confferenct.conference.model.ConfrenceRegistration;

public interface ConfrenceRegistrationRepository extends  MongoRepository<ConfrenceRegistration, String> {
  Page<ConfrenceRegistration>  findAllByUserEmail(String email, Pageable pageable);
    
}
