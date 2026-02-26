package cci.confferenct.conference.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cci.confferenct.conference.model.Confrence;

public interface  ConfrenceRepository extends  MongoRepository<Confrence, String> {}
