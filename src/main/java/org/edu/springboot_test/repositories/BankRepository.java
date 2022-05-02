package org.edu.springboot_test.repositories;

import org.edu.springboot_test.models.Bank;

import java.util.List;

public interface BankRepository {
    List<Bank> findAll();

    Bank findById(Long id);

    void update(Bank account);

}
