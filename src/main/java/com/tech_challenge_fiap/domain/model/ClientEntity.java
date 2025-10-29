package com.tech_challenge_fiap.domain.model;

import com.tech_challenge_fiap.domain.exception.CpfWrongFormat;
import com.tech_challenge_fiap.domain.exception.EmailCannotBeNullOrEmptyException;
import com.tech_challenge_fiap.domain.exception.NameCannotBeNullOrEmptyException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static java.util.Objects.isNull;

@Getter
@Builder
public class ClientEntity {
    @Setter
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String cpf;

    @NonNull
    private String email;

    public ClientEntity(String id, @NonNull String name, @NonNull String cpf, @NonNull String email) {
        this.id = id;
        this.name = name;
        this.cpf = cpf.replaceAll("[^0-9]", "");
        this.email = email;
    }

    public static ClientEntityBuilder builder() {
        return new CustomClientEntityBuilder();
    }

    private static class CustomClientEntityBuilder extends ClientEntityBuilder {
        @Override
        public ClientEntity build() {
            validateName();
            validateEmail();
            validateCpf();
            return super.build();
        }

        private void validateName() {
            if (isNull(super.name) || super.name.trim().isEmpty()) {
                throw new NameCannotBeNullOrEmptyException();
            }
        }

        private void validateEmail() {
            if (isNull(super.email) || super.email.trim().isEmpty()) {
                throw new EmailCannotBeNullOrEmptyException();
            }
        }

        private void validateCpf() {
            var cpfFormated = super.cpf.replaceAll("[^0-9]", "");

            if (cpfFormated.length() != 11) {
                throw new CpfWrongFormat("Formato de CPF inválido.");
            }

            super.cpf = cpfFormated;
        }
    }
}
