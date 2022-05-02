package org.edu.springboot_test.repositories;

import org.edu.springboot_test.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a where a.person=?1")
    Optional<Account> findByPerson(String person);

}
