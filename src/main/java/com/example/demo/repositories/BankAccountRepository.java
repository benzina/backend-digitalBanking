package com.example.demo.repositories;

import com.example.demo.entities.BankAccount;
import com.example.demo.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
