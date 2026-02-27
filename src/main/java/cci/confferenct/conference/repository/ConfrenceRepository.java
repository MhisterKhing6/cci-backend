package cci.confferenct.conference.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import cci.confferenct.conference.model.Confrence;

public interface  ConfrenceRepository extends  MongoRepository<Confrence, String> {
    Page<Confrence> findByEndDateGreaterThan(long endDate, Pageable pageable);
}
