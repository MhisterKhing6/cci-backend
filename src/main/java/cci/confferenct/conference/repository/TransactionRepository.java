package cci.confferenct.conference.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import cci.confferenct.conference.model.Transaction;

public interface  TransactionRepository  extends  MongoRepository<Transaction, String>{
    Optional<Transaction> findByReference(String reference);
    Page<Transaction> findAllByUserInfoEmail(String email,Pageable pageable);
}
