package com.tech_challenge_fiap.application.service;

import com.tech_challenge_fiap.application.port.out.ClientGateway;
import com.tech_challenge_fiap.domain.exception.ClientNotFoundException;
import com.tech_challenge_fiap.domain.exception.CpfAlreadyExistsException;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentCaptor.forClass;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientGateway clientGateway;

    @InjectMocks
    private ClientService clientService;

    private ClientEntity sample;
    private static final String CPF_VALID = "12345678901";

    @BeforeEach
    void setUp() {
        sample = ClientEntity.builder()
                .name("Test")
                .cpf(CPF_VALID)
                .email("test@example.com")
                .build();
        sample.setId("1");
    }

    @Test
    void create_shouldThrow_whenCpfExists() {
        when(clientGateway.findByCpf(sample.getCpf())).thenReturn(sample);
        assertThatThrownBy(() -> clientService.create(sample)).isInstanceOf(CpfAlreadyExistsException.class);
        verify(clientGateway, never()).save(any());
    }

    @Test
    void create_shouldSave_whenCpfDoesNotExist() {
        when(clientGateway.findByCpf(sample.getCpf())).thenReturn(null);
        when(clientGateway.save(any())).thenReturn(sample);
        ClientEntity saved = clientService.create(sample);
        assertThat(saved).isNotNull();
        ArgumentCaptor<ClientEntity> captor = forClass(ClientEntity.class);
        verify(clientGateway).save(captor.capture());
        assertThat(captor.getValue().getCpf()).isEqualTo(CPF_VALID);
    }

    @Test
    void update_shouldThrow_whenNotExists() {
        when(clientGateway.existsById("1")).thenReturn(false);
        assertThatThrownBy(() -> clientService.update("1", sample)).isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    void update_shouldSave_whenExists() {
        when(clientGateway.existsById("1")).thenReturn(true);
        when(clientGateway.save(any())).thenReturn(sample);
        ClientEntity updated = clientService.update("1", sample);
        assertThat(updated).isNotNull();
        ArgumentCaptor<ClientEntity> captor = forClass(ClientEntity.class);
        verify(clientGateway).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo("1");
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        when(clientGateway.existsById("1")).thenReturn(false);
        assertThatThrownBy(() -> clientService.delete("1")).isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    void delete_shouldCallGateway_whenExists() {
        when(clientGateway.existsById("1")).thenReturn(true);
        clientService.delete("1");
        verify(clientGateway).deleteById("1");
    }

    @Test
    void findById_shouldThrow_whenNull() {
        when(clientGateway.findById("1")).thenReturn(null);
        assertThatThrownBy(() -> clientService.findById("1")).isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    void findById_shouldReturn_whenFound() {
        when(clientGateway.findById("1")).thenReturn(sample);
        ClientEntity found = clientService.findById("1");
        assertThat(found).isEqualTo(sample);
    }

    @Test
    void findAll_shouldReturnList() {
        when(clientGateway.findAll()).thenReturn(List.of(sample));
        List<ClientEntity> list = clientService.findAll();
        assertThat(list).hasSize(1);
    }
}
