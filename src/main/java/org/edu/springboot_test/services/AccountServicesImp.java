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
        return accountRepository.findById(id);
    }

    @Override
    public int checkTotalTransfers(Long bankId) {
        Bank bank = bankRepository.findById(bankId);
        return bank.getTotalTransfers();
    }

    @Override
    public BigDecimal checkBalance(Long accountId) {
        Account account = accountRepository.findById(accountId);
        return account.getBalance();
    }

    @Override
    public void transfer(Long numOriginAccount, Long numDestinyAccount, BigDecimal amount, Long bankId) {
        Account accountOrigin = accountRepository.findById(numOriginAccount);
        accountOrigin.debit(amount);
        accountRepository.update(accountOrigin);

        Account destinyAccount = accountRepository.findById(numDestinyAccount);
        destinyAccount.credit(amount);
        accountRepository.update(destinyAccount);

        Bank bank = bankRepository.findById(bankId);
        int totalTransfers = bank.getTotalTransfers();
        bank.setTotalTransfers(++totalTransfers);
        bankRepository.update(bank);
    }
}
