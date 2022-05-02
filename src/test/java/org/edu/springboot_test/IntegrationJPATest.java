package org.edu.springboot_test;

import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegrationJPATest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findByIdTest() {
        Optional<Account> account = accountRepository.findById(1L);
        assertTrue(account.isPresent());
        assertEquals("Andres", account.orElseThrow().getPerson());
    }

    @Test
    void findByPersonTest() {
        Optional<Account> account = accountRepository.findByPerson("Andres");
        assertTrue(account.isPresent());
        assertEquals("Andres", account.orElseThrow().getPerson());
        assertEquals("1000.00", account.orElseThrow().getBalance().toPlainString());
    }

    @Test
    void findByThrowExceptionTest() {
        Optional<Account> account = accountRepository.findByPerson("Rod");
        assertThrows(NoSuchElementException.class, account::orElseThrow);
        assertFalse(account.isPresent());
    }

    @Test
    void findAll() {
        List<Account> account = accountRepository.findAll();
        assertFalse(account.isEmpty());
        assertEquals(2, account.size());
    }

    @Test
    void saveTest() {
        Account pepeAccount = new Account(null, "Pepe", new BigDecimal("3000"));
        Account account = accountRepository.save(pepeAccount);
        assertEquals("Pepe", account.getPerson());
        assertEquals("3000", account.getBalance().toPlainString());
    }

    @Test
    void updateTest() {
        Account pepeAccount = new Account(null, "Pepe", new BigDecimal("3000"));

        Account account = accountRepository.save(pepeAccount);

        assertEquals("Pepe", account.getPerson());
        assertEquals("3000", account.getBalance().toPlainString());

        account.setBalance(new BigDecimal("3800"));
        Account updatedAccount = accountRepository.save(account);

        assertEquals("Pepe", updatedAccount.getPerson());
        assertEquals("3800", updatedAccount.getBalance().toPlainString());
    }

    @Test
    void deleteTest() {
        Account account = accountRepository.findById(2L).orElseThrow();
        assertEquals("John", account.getPerson());

        accountRepository.delete(account);

        assertThrows(NoSuchElementException.class, () -> accountRepository.findByPerson("John").orElseThrow());

        assertEquals(1, accountRepository.findAll().size());
    }
}
