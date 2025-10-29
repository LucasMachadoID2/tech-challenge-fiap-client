package com.tech_challenge_fiap.domain.exception;

public class CpfWrongFormat extends RuntimeException {
    public CpfWrongFormat(String s) {
        super("CPF should have 11 digits");
    }
}
