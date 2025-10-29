package com.tech_challenge_fiap.adapters.out.repository;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "client")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDataModel {

    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String cpf;
    private String email;
}
