package org.edu.springboot_test;

import org.edu.springboot_test.exceptions.NotEnoughMoneyException;
import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.models.Bank;
import org.edu.springboot_test.repositories.AccountRepository;
import org.edu.springboot_test.repositories.BankRepository;
import org.edu.springboot_test.services.AccountServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.edu.springboot_test.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

    @MockBean
    AccountRepository accountRepository;
    @MockBean
    BankRepository bankRepository;

    @Autowired
    AccountServices accountServices;

    @Test
    void contextLoads() {
        when(accountRepository.findById(1L)).thenReturn(createAccount001());
        when(accountRepository.findById(2L)).thenReturn(createAccount002());
        when(bankRepository.findById(1L)).thenReturn(createBank());

        BigDecimal originBalance = accountServices.checkBalance(1L);
        BigDecimal destinyBalance = accountServices.checkBalance(2L);

        assertEquals("1000", originBalance.toPlainString());
        assertEquals("2000", destinyBalance.toPlainString());

        accountServices.transfer(1L, 2L, new BigDecimal("100"), 1L);

        originBalance = accountServices.checkBalance(1L);
        destinyBalance = accountServices.checkBalance(2L);

        assertEquals("900", originBalance.toPlainString());
        assertEquals("2100", destinyBalance.toPlainString());

        int total = accountServices.checkTotalTransfers(1L);
        assertEquals(1, total);
        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(3)).findById(2L);
        verify(accountRepository, times(2)).update(any(Account.class));

        verify(bankRepository, times(2)).findById(1L);
        verify(bankRepository).update(any(Bank.class));

        verify(accountRepository, times(6)).findById(anyLong());
        verify(accountRepository, never()).findAll();
    }

    @Test
    void contextLoads2() {
        when(accountRepository.findById(1L)).thenReturn(createAccount001());
        when(accountRepository.findById(2L)).thenReturn(createAccount002());
        when(bankRepository.findById(1L)).thenReturn(createBank());

        BigDecimal originBalance = accountServices.checkBalance(1L);
        BigDecimal destinyBalance = accountServices.checkBalance(2L);

        assertEquals("1000", originBalance.toPlainString());
        assertEquals("2000", destinyBalance.toPlainString());

        assertThrows(NotEnoughMoneyException.class, () -> {
            accountServices.transfer(1L, 2L, new BigDecimal("1200"), 1L);
        });


        originBalance = accountServices.checkBalance(1L);
        destinyBalance = accountServices.checkBalance(2L);

        assertEquals("1000", originBalance.toPlainString());
        assertEquals("2000", destinyBalance.toPlainString());

        int total = accountServices.checkTotalTransfers(1L);
        assertEquals(0, total);
        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(2)).findById(2L);
        verify(accountRepository, never()).update(any(Account.class));

        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, never()).update(any(Bank.class));
        verify(accountRepository, times(5)).findById(anyLong());
    }

    @Test
    void contextLoads3() {
        when(accountRepository.findById(1L)).thenReturn(createAccount001());

        Account account1 = accountServices.findById(1L);
        Account account2 = accountServices.findById(1L);
        assertSame(account1, account2);
        assertTrue(account1 == account2);
        assertEquals("Andres", account1.getPerson());
        assertEquals("Andres", account2.getPerson());

    }
}
