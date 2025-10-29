package com.tech_challenge_fiap.adapters.out.mappers;

import com.tech_challenge_fiap.adapters.out.repository.ClientDataModel;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientOutMapper {

    public static ClientEntity toEntity(ClientDataModel entity) {
        return ClientEntity.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cpf(entity.getCpf())
                .email(entity.getEmail())
                .build();
    }

    public static ClientDataModel toDataModel(ClientEntity clientEntity) {
        return ClientDataModel.builder()
                .id(clientEntity.getId())
                .name(clientEntity.getName())
                .cpf(clientEntity.getCpf())
                .email(clientEntity.getEmail())
                .build();
    }
}
