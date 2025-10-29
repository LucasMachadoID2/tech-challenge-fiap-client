package com.tech_challenge_fiap.application.port.out;

import com.tech_challenge_fiap.domain.model.ClientEntity;

import java.util.List;

public interface ClientGateway {
    ClientEntity save(ClientEntity client);

    void deleteById(String id);

    ClientEntity findById(String id);

    List<ClientEntity> findAll();

    ClientEntity findByCpf(String cpf);

    boolean existsById(String id);
}
