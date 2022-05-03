package org.edu.springboot_test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.models.TransactionDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void transferTest() throws JsonProcessingException {
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(new BigDecimal("100"));
        dto.setDestinyAccountDTO(2L);
        dto.setOriginAccountDTO(1L);
        dto.setBankId(1L);

        ResponseEntity<String> response = client.postForEntity("/api/accounts/transfer", dto, String.class);

        String json = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("transfer success!"));


        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("transfer success!", jsonNode.path("message").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaction").path("amount").asText());
        assertEquals(1L, jsonNode.path("transaction").path("originAccountDTO").asLong());
    }

    @Test
    @Order(2)
    void detailTest() {
        ResponseEntity<Account> response = client.getForEntity("/api/accounts/1", Account.class);
        Account account = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals("Andres", account.getPerson());
        assertEquals(1L, account.getId());
        assertEquals("900.00", account.getBalance().toPlainString());
    }

    @Test
    @Order(3)
    void listTest() {
        ResponseEntity<Account[]> response = client.getForEntity("/api/accounts/", Account[].class);
        List<Account> accounts = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals(2, accounts.size());
        assertEquals(1L, accounts.get(0).getId());
        assertEquals("Andres", accounts.get(0).getPerson());
        assertEquals("900.00", accounts.get(0).getBalance().toPlainString());
        assertEquals("2100.00", accounts.get(1).getBalance().toPlainString());
        assertEquals("John", accounts.get(1).getPerson());
        assertEquals(2L, accounts.get(1).getId());
    }

    @Test
    @Order(4)
    void saveTest() {
        Account account = new Account(null, "Pepa", new BigDecimal("3800"));

        ResponseEntity<Account> response = client.postForEntity("/api/accounts", account, Account.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Account createdAccount = response.getBody();
        assertNotNull(createdAccount);
        assertEquals(3L, createdAccount.getId());
        assertEquals("Pepa", createdAccount.getPerson());
        assertEquals("3800", createdAccount.getBalance().toPlainString());
    }

    @Test
    @Order(5)
    void deleteTest() {

        ResponseEntity<Account[]> response = client.getForEntity("/api/accounts", Account[].class);
        List<Account> accounts = Arrays.asList(response.getBody());
        assertEquals(3, accounts.size());

        //client.delete("/api/accounts/3"));
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);
        ResponseEntity<Void> exchange = client.exchange("/api/accounts/{id}", HttpMethod.DELETE, null, Void.class,
                pathVariables);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        response = client.getForEntity("/api/accounts", Account[].class);
        accounts = Arrays.asList(response.getBody());
        assertEquals(2, accounts.size());

        ResponseEntity<Account> detailResponse = client.getForEntity("/api/accounts/3", Account.class);
        assertEquals(HttpStatus.NOT_FOUND, detailResponse.getStatusCode());
        assertFalse(detailResponse.hasBody());
    }
}