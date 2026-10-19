package com.michael.bankmonitoring.repository;

import com.michael.bankmonitoring.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

