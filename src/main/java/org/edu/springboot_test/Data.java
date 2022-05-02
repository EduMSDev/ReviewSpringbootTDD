package org.edu.springboot_test;

import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.models.Bank;

import java.math.BigDecimal;
import java.util.Optional;

public class Data {
    public static Optional<Account> createAccount001() {
        return Optional.of(new Account(1L, "Andres", new BigDecimal("1000")));
    }

    public static Optional<Account> createAccount002() {
        return Optional.of(new Account(2L, "John", new BigDecimal("2000")));
    }

    public static Optional<Bank> createBank() {
        return Optional.of(new Bank(1L, "Banco financiero", 0));
    }
}
