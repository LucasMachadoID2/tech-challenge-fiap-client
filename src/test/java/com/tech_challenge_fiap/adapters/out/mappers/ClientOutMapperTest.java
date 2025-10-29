package com.tech_challenge_fiap.adapters.out.mappers;

import com.tech_challenge_fiap.adapters.out.repository.ClientDataModel;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClientOutMapperTest {

    @Test
    void toEntity_shouldMapAllFields() {
        ClientDataModel model = ClientDataModel.builder()
                .id("1")
                .name("A")
                .cpf("12345678901")
                .email("a@a.com")
                .build();

        ClientEntity e = ClientOutMapper.toEntity(model);
        assertThat(e.getId()).isEqualTo("1");
        assertThat(e.getName()).isEqualTo("A");
        assertThat(e.getCpf()).isEqualTo("12345678901");
        assertThat(e.getEmail()).isEqualTo("a@a.com");
    }

    @Test
    void toDataModel_shouldMapAllFields() {
        ClientEntity e = ClientEntity.builder()
                .name("A")
                .cpf("12345678901")
                .email("a@a.com")
                .build();
        e.setId("1");

        ClientDataModel model = ClientOutMapper.toDataModel(e);
        assertThat(model.getId()).isEqualTo("1");
        assertThat(model.getName()).isEqualTo("A");
        assertThat(model.getCpf()).isEqualTo("12345678901");
        assertThat(model.getEmail()).isEqualTo("a@a.com");
    }
}
