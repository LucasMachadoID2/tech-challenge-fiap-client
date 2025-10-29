package com.tech_challenge_fiap.application.service;

import com.tech_challenge_fiap.application.port.in.ClientUseCase;
import com.tech_challenge_fiap.application.port.out.ClientGateway;
import com.tech_challenge_fiap.domain.exception.ClientNotFoundException;
import com.tech_challenge_fiap.domain.exception.CpfAlreadyExistsException;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientUseCase {

    private final ClientGateway clientGateway;
    private static final String CLIENT_NOT_FOUND_MESSAGE = "Cliente não encontrado para o id: ";

    @Override
    public ClientEntity create(ClientEntity clientEntity) {
        ClientEntity existing = clientGateway.findByCpf(clientEntity.getCpf());
        if (nonNull(existing)) {
            throw new CpfAlreadyExistsException(clientEntity.getCpf());
        }
        return clientGateway.save(clientEntity);
    }

    @Override
    public ClientEntity update(String id, ClientEntity clientEntity) {
        if (!clientGateway.existsById(id)) {
            throw new ClientNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id);
        }
        clientEntity.setId(id);
        return clientGateway.save(clientEntity);
    }

    @Override
    public void delete(String id) {
        if (!clientGateway.existsById(id)) {
            throw new ClientNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id);
        }
        clientGateway.deleteById(id);
    }

    @Override
    public ClientEntity findById(String id) {
        ClientEntity client = clientGateway.findById(id);
        if (isNull(client)) {
            throw new ClientNotFoundException(CLIENT_NOT_FOUND_MESSAGE + id);
        }
        return client;
    }

    @Override
    public List<ClientEntity> findAll() {
        return clientGateway.findAll();
    }
}
