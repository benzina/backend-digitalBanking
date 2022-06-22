package com.example.demo.services;

import com.example.demo.dtos.*;
import com.example.demo.entities.BankAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankAccountService {
    CustomerDto saveCustomer(CustomerDto customerDto);
    CustomerDto getCustomer(Long id);
    CustomerDto updateCustomer(CustomerDto customerDto);
    void deleteCustomer(Long id);
    CurrentBankAccountDto saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId);
    SavingBankAccountDto saveSavingBankAccount(double initialBalance, double interestRate, Long customerId);
    List<CustomerDto> listCustomers();
    BankAccountDto getBankAccountDto(String accountId);
    BankAccount getBankAccount(String accountId);
    void debit(String accountId,double mount, String description);
    void credit(String accountId,double mount, String description);
    void transfer(String accountIdSource,String accountIdDestination,double amount);
    List<BankAccountDto> BANK_ACCOUNT_LIST();
    List<OperationDto> getOperations();
    List<OperationDto> accountHistory(String accountId);


    AccountHistoryDto getAccountHistory(String accountId, int page, int size);
}
