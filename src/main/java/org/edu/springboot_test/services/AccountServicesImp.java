package org.edu.springboot_test.services;

import org.edu.springboot_test.models.Account;
import org.edu.springboot_test.models.Bank;
import org.edu.springboot_test.repositories.AccountRepository;
import org.edu.springboot_test.repositories.BankRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServicesImp implements AccountServices {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    public AccountServicesImp(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public int checkTotalTransfers(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTotalTransfers();
    }

    @Override
    public BigDecimal checkBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return account.getBalance();
    }

    @Override
    public void transfer(Long numOriginAccount, Long numDestinyAccount, BigDecimal amount, Long bankId) {
        Account accountOrigin = accountRepository.findById(numOriginAccount).orElseThrow();
        accountOrigin.debit(amount);
        accountRepository.save(accountOrigin);

        Account destinyAccount = accountRepository.findById(numDestinyAccount).orElseThrow();
        destinyAccount.credit(amount);
        accountRepository.save(destinyAccount);

        Bank bank = bankRepository.findById(bankId).orElseThrow();
        int totalTransfers = bank.getTotalTransfers();
        bank.setTotalTransfers(++totalTransfers);
        bankRepository.save(bank);
    }
}
