package org.edu.springboot_test.controller;

import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.models.TransactionDTO;
import org.edu.springboot_test.services.AccountServicesImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountServicesImp accountServices;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> list() {
        return accountServices.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account save(@RequestBody Account account) {
        return accountServices.save(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> detail(@PathVariable Long id) {
        Account account;
        try {
            account = accountServices.findById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> transfer(@RequestBody TransactionDTO transactionDTO) {
        accountServices.transfer(transactionDTO.getOriginAccountDTO(), transactionDTO.getDestinyAccountDTO(),
                transactionDTO.getAmount(), transactionDTO.getBankId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "transfer success!");
        response.put("transaction", transactionDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accountServices.deleteById(id);
    }


}
