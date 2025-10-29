package com.tech_challenge_fiap.adapters.in.mappers;

import com.tech_challenge_fiap.adapters.in.dtos.ClientRequestDto;
import com.tech_challenge_fiap.adapters.in.dtos.ClientResponseDto;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientInMapper {

    public static ClientEntity toEntity(ClientRequestDto dto) {
        return ClientEntity.builder()
                .name(dto.getName())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .build();
    }

    public static ClientResponseDto toResponse(ClientEntity clientEntity) {
        return ClientResponseDto.builder()
                .id(clientEntity.getId())
                .name(clientEntity.getName())
                .cpf(clientEntity.getCpf())
                .email(clientEntity.getEmail())
                .build();
    }
}
