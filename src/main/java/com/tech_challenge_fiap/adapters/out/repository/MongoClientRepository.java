package com.tech_challenge_fiap.adapters.out.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoClientRepository extends MongoRepository<ClientDataModel, String> {

    ClientDataModel findByCpf(String cpf);
}

