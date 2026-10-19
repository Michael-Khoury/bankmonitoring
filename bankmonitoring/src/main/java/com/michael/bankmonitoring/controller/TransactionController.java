package com.michael.bankmonitoring.controller;

import com.michael.bankmonitoring.model.Account;
import com.michael.bankmonitoring.model.Transaction;
import com.michael.bankmonitoring.repository.AccountRepository;
import com.michael.bankmonitoring.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;
import com.michael.bankmonitoring.service.FraudCheckService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;

    private final FraudCheckService fraudCheckService;

    public TransactionController(TransactionRepository transactionRepo,
                                 AccountRepository accountRepo,
                                 FraudCheckService fraudCheckService) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.fraudCheckService = fraudCheckService;
    }


    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        transaction.setTimestamp(LocalDateTime.now());

        Account account = accountRepo.findById(transaction.getAccount().getId()).orElseThrow();
        transaction.setAccount(account);

        if (fraudCheckService.isSuspicious(transaction)) {
            System.out.println("⚠️ Potential Fraud Detected!");
        }

        return transactionRepo.save(transaction);
    }


    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }
}
