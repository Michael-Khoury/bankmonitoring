package com.michael.bankmonitoring.controller;

import com.michael.bankmonitoring.model.Account;
import com.michael.bankmonitoring.repository.AccountRepository;
import com.michael.bankmonitoring.repository.UserRepository;
import com.michael.bankmonitoring.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepo;
    private final UserRepository userRepo;

    public AccountController(AccountRepository accountRepo, UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        // validate user
        Long userId = account.getUser().getId();
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        account.setUser(user);
        return accountRepo.save(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }
}
