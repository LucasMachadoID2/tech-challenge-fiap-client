package com.tech_challenge_fiap.adapters.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech_challenge_fiap.adapters.in.dtos.ClientRequestDto;
import com.tech_challenge_fiap.application.port.in.ClientUseCase;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import com.tech_challenge_fiap.domain.exception.ClientNotFoundException;
import com.tech_challenge_fiap.domain.exception.CpfAlreadyExistsException;
import com.tech_challenge_fiap.adapters.in.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClientControllerTest {

    private MockMvc mockMvc;
    private ClientUseCase clientUseCase;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        clientUseCase = Mockito.mock(ClientUseCase.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ClientController(clientUseCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    private static ClientEntity entity(String id, String name, String cpf, String email) {
        ClientEntity e = ClientEntity.builder()
                .name(name)
                .cpf(cpf)
                .email(email)
                .build();
        e.setId(id);
        return e;
    }

    @Test
    void list_shouldReturn200_andJsonArray() throws Exception {
        Mockito.when(clientUseCase.findAll()).thenReturn(List.of(
                entity("1","A","12345678901","a@a.com"),
                entity("2","B","12345678902","b@b.com")
        ));

        mockMvc.perform(get("/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].name", is("A")))
                .andExpect(jsonPath("$[0].cpf", is("12345678901")))
                .andExpect(jsonPath("$[0].email", is("a@a.com")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].name", is("B")))
                .andExpect(jsonPath("$[1].cpf", is("12345678902")))
                .andExpect(jsonPath("$[1].email", is("b@b.com")));
    }

    @Test
    void get_shouldReturn200_whenFound() throws Exception {
        Mockito.when(clientUseCase.findById("1")).thenReturn(entity("1","A","12345678901","a@a.com"));

        mockMvc.perform(get("/v1/clients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("A")))
                .andExpect(jsonPath("$.cpf", is("12345678901")))
                .andExpect(jsonPath("$.email", is("a@a.com")));
    }

    @Test
    void get_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(clientUseCase.findById("999")).thenThrow(new ClientNotFoundException("not found"));
        mockMvc.perform(get("/v1/clients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201_whenValid() throws Exception {
        ClientRequestDto req = new ClientRequestDto("A","123.456.789-01","a@a.com");
        ClientEntity saved = entity("10","A","12345678901","a@a.com");
        Mockito.when(clientUseCase.create(any())).thenReturn(saved);

        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/v1/clients/10"))
                .andExpect(jsonPath("$.id", is("10")))
                .andExpect(jsonPath("$.name", is("A")))
                .andExpect(jsonPath("$.cpf", is("12345678901")))
                .andExpect(jsonPath("$.email", is("a@a.com")));
    }

    @Test
    void create_shouldReturn409_whenCpfExists() throws Exception {
        ClientRequestDto req = new ClientRequestDto("A","123.456.789-01","a@a.com");
        Mockito.when(clientUseCase.create(any())).thenThrow(new CpfAlreadyExistsException("12345678901"));

        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void create_shouldReturn400_whenPayloadInvalid() throws Exception {
        // name vazio e email inválido
        ClientRequestDto req = new ClientRequestDto(" ","123.456.789-01","invalid");
        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void create_shouldReturn400_whenMultipleFieldsInvalid() throws Exception {
        // name blank, cpf blank, email inválido
        ClientRequestDto req = new ClientRequestDto(" "," ","not-an-email");
        mockMvc.perform(post("/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.cpf").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void update_shouldReturn200_whenValid() throws Exception {
        ClientRequestDto req = new ClientRequestDto("A","123.456.789-01","a@a.com");
        ClientEntity updated = entity("1","A","12345678901","a@a.com");
        Mockito.when(clientUseCase.update(eq("1"), any())).thenReturn(updated);

        mockMvc.perform(put("/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("A")))
                .andExpect(jsonPath("$.cpf", is("12345678901")))
                .andExpect(jsonPath("$.email", is("a@a.com")));
    }

    @Test
    void update_shouldReturn400_whenPayloadInvalid() throws Exception {
        ClientRequestDto req = new ClientRequestDto(" ","123","bad");
        mockMvc.perform(put("/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/v1/clients/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(clientUseCase).delete("1");
    }

    @Test
    void update_shouldReturn404_whenNotFound() throws Exception {
        ClientRequestDto req = new ClientRequestDto("A","123.456.789-01","a@a.com");
        Mockito.when(clientUseCase.update(eq("999"), any())).thenThrow(new ClientNotFoundException("not found"));
        mockMvc.perform(put("/v1/clients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn404_whenNotFound() throws Exception {
        Mockito.doThrow(new ClientNotFoundException("not found")).when(clientUseCase).delete("999");
        mockMvc.perform(delete("/v1/clients/999"))
                .andExpect(status().isNotFound());
    }
}
