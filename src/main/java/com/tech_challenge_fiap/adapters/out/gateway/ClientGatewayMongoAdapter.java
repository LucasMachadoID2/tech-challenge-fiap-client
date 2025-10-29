package com.tech_challenge_fiap.adapters.out.gateway;

import com.tech_challenge_fiap.adapters.out.mappers.ClientOutMapper;
import com.tech_challenge_fiap.adapters.out.repository.ClientDataModel;
import com.tech_challenge_fiap.adapters.out.repository.MongoClientRepository;
import com.tech_challenge_fiap.application.port.out.ClientGateway;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.tech_challenge_fiap.adapters.out.mappers.ClientOutMapper.toDataModel;
import static com.tech_challenge_fiap.adapters.out.mappers.ClientOutMapper.toEntity;

@Component
@RequiredArgsConstructor
public class ClientGatewayMongoAdapter implements ClientGateway {

    private final MongoClientRepository clientRepository;

    @Override
    public ClientEntity save(ClientEntity clientEntity) {
        ClientDataModel client = clientRepository.save(toDataModel(clientEntity));
        return toEntity(client);
    }

    @Override
    public void deleteById(String id) {
        clientRepository.deleteById(id);
    }

    @Override
    public ClientEntity findById(String id) {
        return clientRepository.findById(id)
                .map(ClientOutMapper::toEntity)
                .orElse(null);
    }

    @Override
    public List<ClientEntity> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientOutMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ClientEntity findByCpf(String cpf) {
        ClientDataModel client = clientRepository.findByCpf(cpf);
        return client == null ? null : toEntity(client);
    }

    @Override
    public boolean existsById(String id) {
        return clientRepository.existsById(id);
    }
}
