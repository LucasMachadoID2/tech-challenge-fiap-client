package com.tech_challenge_fiap.adapters.in.exception;

import com.tech_challenge_fiap.domain.exception.ClientNotFoundException;
import com.tech_challenge_fiap.domain.exception.CpfAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldReturn404OnClientNotFound() {
        ResponseEntity<Object> resp = handler.handleClientNotFound(new ClientNotFoundException("not found"));
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturn409OnCpfConflict() {
        ResponseEntity<Object> resp = handler.handleCpfConflict(new CpfAlreadyExistsException("123"));
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldReturn400OnValidation() throws Exception {
        Object target = new Object();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "target");
        bindingResult.addError(new FieldError("target", "name", "must not be blank"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Object> resp = handler.handleValidation(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) resp.getBody();
        assertThat(body).containsKey("name");
    }
}
