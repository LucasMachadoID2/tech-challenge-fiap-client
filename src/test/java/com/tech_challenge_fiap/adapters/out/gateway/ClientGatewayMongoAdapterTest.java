package com.tech_challenge_fiap.adapters.out.gateway;

import com.tech_challenge_fiap.adapters.out.repository.ClientDataModel;
import com.tech_challenge_fiap.adapters.out.repository.MongoClientRepository;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientGatewayMongoAdapterTest {

    @Mock
    private MongoClientRepository repository;

    private ClientGatewayMongoAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new ClientGatewayMongoAdapter(repository);
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

    private static ClientDataModel model(String id, String name, String cpf, String email) {
        return ClientDataModel.builder()
                .id(id)
                .name(name)
                .cpf(cpf)
                .email(email)
                .build();
    }

    @Test
    void save_shouldMapAndReturnEntity() {
        ClientEntity input = entity("1","A","12345678901","a@a.com");
        ClientDataModel saved = model("1","A","12345678901","a@a.com");
        when(repository.save(any(ClientDataModel.class))).thenReturn(saved);

        ClientEntity result = adapter.save(input);

        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("A");
        assertThat(result.getCpf()).isEqualTo("12345678901");
        assertThat(result.getEmail()).isEqualTo("a@a.com");

        ArgumentCaptor<ClientDataModel> captor = forClass(ClientDataModel.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo("1");
        assertThat(captor.getValue().getName()).isEqualTo("A");
        assertThat(captor.getValue().getCpf()).isEqualTo("12345678901");
        assertThat(captor.getValue().getEmail()).isEqualTo("a@a.com");
    }

    @Test
    void deleteById_shouldDelegate() {
        adapter.deleteById("10");
        verify(repository).deleteById("10");
    }

    @Test
    void findById_shouldReturnEntity_whenFound() {
        when(repository.findById("1")).thenReturn(Optional.of(model("1","A","12345678901","a@a.com")));
        ClientEntity result = adapter.findById("1");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
    }

    @Test
    void findById_shouldReturnNull_whenNotFound() {
        when(repository.findById("999")).thenReturn(Optional.empty());
        ClientEntity result = adapter.findById("999");
        assertThat(result).isNull();
    }

    @Test
    void findAll_shouldReturnMappedList() {
        when(repository.findAll()).thenReturn(List.of(
                model("1","A","12345678901","a@a.com"),
                model("2","B","12345678902","b@b.com")
        ));
        List<ClientEntity> result = adapter.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(1).getId()).isEqualTo("2");
    }

    @Test
    void findByCpf_shouldReturnEntity_whenFound() {
        when(repository.findByCpf("12345678901")).thenReturn(model("1","A","12345678901","a@a.com"));
        ClientEntity result = adapter.findByCpf("12345678901");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
    }

    @Test
    void findByCpf_shouldReturnNull_whenNotFound() {
        when(repository.findByCpf("000")).thenReturn(null);
        ClientEntity result = adapter.findByCpf("000");
        assertThat(result).isNull();
    }
}
