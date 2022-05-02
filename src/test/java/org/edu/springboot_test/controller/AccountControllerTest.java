package org.edu.springboot_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.edu.springboot_test.models.TransactionDTO;
import org.edu.springboot_test.services.AccountServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.edu.springboot_test.Data.createAccount001;
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
    private AccountServices accountServices;

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
}