package org.edu.springboot_test.repositories;

import org.edu.springboot_test.models.Account;

import java.util.List;

public interface AccountRepository {
    List<Account> findAll();

    Account findById(Long id);

    void update(Account account);

}
