package com.tech_challenge_fiap.adapters.in.mappers;

import com.tech_challenge_fiap.adapters.in.dtos.ClientRequestDto;
import com.tech_challenge_fiap.adapters.in.dtos.ClientResponseDto;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClientInMapperTest {

    @Test
    void toEntity_shouldMapAllFields() {
        ClientRequestDto dto = new ClientRequestDto("Name","123.456.789-01","n@n.com");
        ClientEntity e = ClientInMapper.toEntity(dto);
        assertThat(e.getName()).isEqualTo("Name");
        assertThat(e.getCpf()).isEqualTo("12345678901");
        assertThat(e.getEmail()).isEqualTo("n@n.com");
    }

    @Test
    void toResponse_shouldMapAllFields() {
        ClientEntity e = ClientEntity.builder()
                .name("Name")
                .cpf("12345678901")
                .email("n@n.com")
                .build();
        e.setId("55");
        ClientResponseDto dto = ClientInMapper.toResponse(e);
        assertThat(dto.getId()).isEqualTo("55");
        assertThat(dto.getName()).isEqualTo("Name");
        assertThat(dto.getCpf()).isEqualTo("12345678901");
        assertThat(dto.getEmail()).isEqualTo("n@n.com");
    }
}
