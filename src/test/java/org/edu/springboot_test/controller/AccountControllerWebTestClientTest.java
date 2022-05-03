package org.edu.springboot_test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.models.TransactionDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerWebTestClientTest {

    private ObjectMapper objectMapper;
    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void transferTest() throws JsonProcessingException {
        TransactionDTO dto = new TransactionDTO();
        dto.setOriginAccountDTO(1L);
        dto.setDestinyAccountDTO(2L);
        dto.setBankId(1L);
        dto.setAmount(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "transfer success!");
        response.put("transaction", dto);

        client.post().uri("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto).exchange().expectStatus().isOk()
                .expectBody().consumeWith(responseBody -> {
                    try {
                        JsonNode json = objectMapper.readTree(responseBody.getResponseBody());
                        assertEquals("transfer success!", json.path("message").asText());
                        assertEquals(1, json.path("transaction").path("originAccountDTO").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transaction").path("amount").asText());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").isEqualTo("transfer success!")
                .jsonPath("$.transaction.originAccountDTO").isEqualTo(dto.getOriginAccountDTO())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    void detailTest() throws JsonProcessingException {
        Account account = new Account(1L, "Andres", new BigDecimal("900"));

        client.get().uri("/api/accounts/1").exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.person").isEqualTo("Andres")
                .jsonPath("$.balance").isEqualTo(900).json(objectMapper.writeValueAsString(account));
    }

    @Test
    @Order(3)
    void listTest() {
        client.get().uri("/api/accounts").exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
                .jsonPath("$[0].person").isEqualTo("Andres")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].balance").isEqualTo(900)
                .jsonPath("$").isArray().jsonPath("$").value(hasSize(2));
    }

    @Test
    @Order(4)
    void saveTest() {
        Account account = new Account(null, "Pepe", new BigDecimal("3000"));
        client.post().uri("/api/accounts").contentType(MediaType.APPLICATION_JSON).bodyValue(account).
                exchange().expectStatus().isCreated().expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.person").isEqualTo("Pepe")
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.balance").isEqualTo(3000);
    }

    @Test
    @Order(5)
    void deleteTest() {
        client.get().uri("/api/accounts").exchange().expectStatus().isOk()
                .expectBodyList(Account.class).hasSize(3);

        client.delete().uri("/api/accounts/2").exchange().expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/accounts").exchange().expectStatus().isOk()
                .expectBodyList(Account.class).hasSize(2);

        client.get().uri("api/accounts/2").exchange().expectStatus().isNotFound().expectBody().isEmpty();

    }
}