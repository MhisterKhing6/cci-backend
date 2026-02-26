package cci.confferenct.conference.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import cci.confferenct.conference.model.User;

public interface UserRepository  extends MongoRepository<User, String>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
