package com.michael.bankmonitoring.repository;

import com.michael.bankmonitoring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

