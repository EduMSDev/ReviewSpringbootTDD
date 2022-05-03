package org.edu.springboot_test.services;

import org.edu.springboot_test.models.Account;

import java.math.BigDecimal;
import java.util.List;


public interface AccountServices {

    List<Account> findAll();

    Account save(Account account);

    Account findById(Long id);

    int checkTotalTransfers(Long bankId);

    BigDecimal checkBalance(Long accountId);

    void transfer(Long numOriginAccount, Long numDestinyAccount, BigDecimal amount, Long bankId);

    void deleteById(Long id);

}
