package com.tech_challenge_fiap.domain.model;

import com.tech_challenge_fiap.domain.exception.CpfWrongFormat;
import com.tech_challenge_fiap.domain.exception.EmailCannotBeNullOrEmptyException;
import com.tech_challenge_fiap.domain.exception.NameCannotBeNullOrEmptyException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClientEntityTest {

    @Test
    void builder_shouldNormalizeCpfDigitsOnly() {
        ClientEntity e = ClientEntity.builder()
                .name("Name")
                .cpf("123.456.789-01")
                .email("n@n.com")
                .build();
        assertThat(e.getCpf()).isEqualTo("12345678901");
    }

    @Test
    void builder_shouldThrow_whenCpfWrongLength() {
        assertThatThrownBy(() -> ClientEntity.builder()
                .name("Name")
                .cpf("123")
                .email("n@n.com")
                .build())
                .isInstanceOf(CpfWrongFormat.class);
    }

    @Test
    void builder_shouldThrow_whenNameBlank() {
        assertThatThrownBy(() -> ClientEntity.builder()
                .name(" ")
                .cpf("12345678901")
                .email("n@n.com")
                .build())
                .isInstanceOf(NameCannotBeNullOrEmptyException.class);
    }

    @Test
    void builder_shouldThrow_whenEmailBlank() {
        assertThatThrownBy(() -> ClientEntity.builder()
                .name("Name")
                .cpf("12345678901")
                .email(" ")
                .build())
                .isInstanceOf(EmailCannotBeNullOrEmptyException.class);
    }

    @Test
    void builder_shouldThrow_whenNameNull() {
        assertThatThrownBy(() -> ClientEntity.builder()
                .name(null)
                .cpf("12345678901")
                .email("n@n.com")
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void builder_shouldThrow_whenEmailNull() {
        assertThatThrownBy(() -> ClientEntity.builder()
                .name("Name")
                .cpf("12345678901")
                .email(null)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void builder_shouldNormalizeCpf_withSpacesAndDotsAndDash() {
        ClientEntity e = ClientEntity.builder()
                .name("Name")
                .cpf(" 123.456.789-01 ")
                .email("n@n.com")
                .build();
        assertThat(e.getCpf()).isEqualTo("12345678901");
    }
}
