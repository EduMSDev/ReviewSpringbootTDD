package org.edu.springboot_test.services;

import org.edu.springboot_test.models.Account;

import java.math.BigDecimal;

public interface AccountServices {
    Account findById(Long id);

    int checkTotalTransfers(Long bankId);

    BigDecimal checkBalance(Long accountId);

    void transfer(Long numOriginAccount, Long numDestinyAccount, BigDecimal amount);

}
