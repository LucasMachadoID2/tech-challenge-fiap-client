package com.tech_challenge_fiap.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainExceptionsTest {

    @Test
    void clientNotFound_shouldExposeMessage() {
        ClientNotFoundException ex = new ClientNotFoundException("not found");
        assertThat(ex.getMessage()).isEqualTo("not found");
    }

    @Test
    void cpfAlreadyExists_shouldFormatMessage() {
        CpfAlreadyExistsException ex = new CpfAlreadyExistsException("12345678901");
        assertThat(ex.getMessage()).contains("12345678901");
    }

    @Test
    void nameCannotBeNullOrEmpty_shouldHaveMessage() {
        NameCannotBeNullOrEmptyException ex = new NameCannotBeNullOrEmptyException();
        assertThat(ex).hasMessageContaining("Name");
    }

    @Test
    void emailCannotBeNullOrEmpty_shouldHaveMessage() {
        EmailCannotBeNullOrEmptyException ex = new EmailCannotBeNullOrEmptyException();
        assertThat(ex).hasMessageContaining("Email");
    }

    @Test
    void cpfWrongFormat_shouldHaveMessage() {
        CpfWrongFormat ex = new CpfWrongFormat("bad");
        assertThat(ex.getMessage()).isNotBlank();
    }
}
