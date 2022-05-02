package org.edu.springboot_test.repositories;

import org.edu.springboot_test.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {


}
