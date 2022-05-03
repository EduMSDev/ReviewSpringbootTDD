package org.edu.springboot_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.models.TransactionDTO;
import org.edu.springboot_test.services.AccountServicesImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.edu.springboot_test.Data.createAccount001;
import static org.edu.springboot_test.Data.createAccount002;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountServicesImp accountServices;

    @Test
    void detailTest() throws Exception {
        when(accountServices.findById(1L)).thenReturn(createAccount001().orElseThrow());

        mockMvc.perform(get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Andres"))
                .andExpect(jsonPath("$.balance").value("1000"));

        verify(accountServices).findById(1L);
    }

    @Test
    void transferTest() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setOriginAccountDTO(1L);
        transactionDTO.setDestinyAccountDTO(2L);
        transactionDTO.setAmount(new BigDecimal("2000"));
        transactionDTO.setBankId(1L);

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "transfer success!");
        response.put("transaction", transactionDTO);

        mockMvc.perform(post("/api/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("transfer success!"))
                .andExpect(jsonPath("$.transaction.originAccountDTO").value(transactionDTO.getOriginAccountDTO()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void listTest() throws Exception {
        List<Account> accounts = Arrays.asList(createAccount001().orElseThrow(), createAccount002().orElseThrow());
        when(accountServices.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].person").value("Andres"))
                .andExpect(jsonPath("$[0].balance").value("1000"))
                .andExpect(jsonPath("$[1].person").value("John"))
                .andExpect(jsonPath("$[1].balance").value("2000"))
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));
        verify(accountServices).findAll();
    }

    @Test
    void saveTest() throws Exception {
        Account account = new Account(null, "Pepe", new BigDecimal("3000"));
        when(accountServices.save(any())).then(invocation -> {
            Account c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3))).andExpect(jsonPath("$.person", is("Pepe")))
                .andExpect(jsonPath("$.balance", is(3000)));
        verify(accountServices).save(any());
    }
}