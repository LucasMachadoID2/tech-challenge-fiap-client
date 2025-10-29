package com.tech_challenge_fiap.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.tech_challenge_fiap.application.port.in.ClientUseCase;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import io.cucumber.java.Before;
import org.mockito.Mockito;

import java.util.List;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CucumberSpringConfig {

    @MockBean
    private ClientUseCase clientUseCase;

    // Cenário padrão: sem clientes cadastrados
    @Before
    public void setupMocks() {
        Mockito.when(clientUseCase.findAll()).thenReturn(List.of());
    }

    // Cenário com clientes existentes
    @Before("@clientes_existentes")
    public void setupComClientes() {
        ClientEntity cliente = ClientEntity.builder()
                .id("1")
                .name("Maria Silva")
                .cpf("12345678900")
                .email("maria@email.com")
                .build();

        ClientEntity cliente2 = ClientEntity.builder()
                .id("2")
                .name("João Souza")
                .cpf("98765432100")
                .email("joao@email.com")
                .build();

        Mockito.when(clientUseCase.findAll()).thenReturn(List.of(cliente, cliente2));
    }
}
