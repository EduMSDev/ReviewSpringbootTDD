package org.edu.springboot_test;

import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.models.Bank;

import java.math.BigDecimal;

public class Data {
    public static Account createAccount001() {
        return new Account(1L, "Andres", new BigDecimal("1000"));
    }

    public static Account createAccount002() {
        return new Account(2L, "John", new BigDecimal("2000"));
    }

    public static Bank createBank() {
        return new Bank(1L, "Banco financiero", 0);

    }
}
