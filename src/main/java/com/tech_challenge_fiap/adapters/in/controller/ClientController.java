package com.tech_challenge_fiap.adapters.in.controller;

import com.tech_challenge_fiap.adapters.in.dtos.ClientRequestDto;
import com.tech_challenge_fiap.adapters.in.dtos.ClientResponseDto;
import com.tech_challenge_fiap.adapters.in.mappers.ClientInMapper;
import com.tech_challenge_fiap.application.port.in.ClientUseCase;
import com.tech_challenge_fiap.domain.model.ClientEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.net.URI;

import static com.tech_challenge_fiap.adapters.in.mappers.ClientInMapper.toEntity;
import static com.tech_challenge_fiap.adapters.in.mappers.ClientInMapper.toResponse;

@Tag(name = "Client", description = "Operations related to client management")
@RestController
@RequestMapping("/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientUseCase clientUseCase;

    @GetMapping
    @Operation(summary = "List all clients")
    @ApiResponse(responseCode = "200", description = "Clients retrieved",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientResponseDto.class)))
    public ResponseEntity<List<ClientResponseDto>> list() {
        List<ClientEntity> clientEntities = clientUseCase.findAll();
        List<ClientResponseDto> response = clientEntities.stream()
                .map(ClientInMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by id")
    @ApiResponse(responseCode = "200", description = "Client found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<ClientResponseDto> get(@PathVariable String id) {
        ClientEntity client = clientUseCase.findById(id);
        return ResponseEntity.ok(toResponse(client));
    }

    @PostMapping
    @Operation(summary = "Create a new client")
    @ApiResponse(responseCode = "201", description = "Client created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "CPF already exists")
    public ResponseEntity<ClientResponseDto> create(@RequestBody @Valid ClientRequestDto request) {
        ClientEntity created = clientUseCase.create(toEntity(request));
        URI location = URI.create("/v1/clients/" + created.getId());
        return ResponseEntity.created(location).body(toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client")
    @ApiResponse(responseCode = "200", description = "Client updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<ClientResponseDto> update(@PathVariable String id, @RequestBody @Valid ClientRequestDto request) {
        ClientEntity updated = clientUseCase.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client")
    @ApiResponse(responseCode = "204", description = "Client deleted")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        clientUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
