package com.michael.bankmonitoring.service;

import com.michael.bankmonitoring.model.Transaction;
import com.michael.bankmonitoring.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudCheckService {

    private final TransactionRepository transactionRepo;

    public FraudCheckService(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public boolean isSuspicious(Transaction newTxn) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        List<Transaction> recent = transactionRepo.findAll().stream()
                .filter(t -> t.getAccount().getId().equals(newTxn.getAccount().getId()))
                .filter(t -> t.getTimestamp().isAfter(oneMinuteAgo))
                .filter(t -> t.getAmount() > 1000)
                .toList();

        return recent.size() >= 3;
    }
}

