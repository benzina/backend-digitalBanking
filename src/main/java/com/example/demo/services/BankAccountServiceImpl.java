package com.example.demo.services;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BalanceNotSufficentException;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.mappers.BankAcountMapper;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private OperationRepository operationRepository;
    private BankAcountMapper bankAcountMapper;

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        log.info("Saving new customer");
        Customer customer = bankAcountMapper.toCustomer(customerDto);
        Customer customerSaved = customerRepository.save(customer);
        return bankAcountMapper.fromCustomer(customerSaved);
    }

    @Override
    public CustomerDto getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFoundException("customer id : "+id+" is not found"));
        return bankAcountMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        log.info("Succed updating  customer ");
        Customer customer = customerRepository.findById(customerDto.getId())
                .orElseThrow(()-> new CustomerNotFoundException("customer id : "+customerDto.getId()+" is not found"));
        BeanUtils.copyProperties(customer,customerDto);
        return bankAcountMapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public CurrentBankAccountDto saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);
        return bankAcountMapper.fromCurrentBankAccount(savedCurrentAccount);
    }

    @Override
    public SavingBankAccountDto saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        SavingAccount savedSavingAccount=  bankAccountRepository.save(savingAccount);
        return bankAcountMapper.fromSavingBankAccount(savedSavingAccount);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        List<Customer> customers= customerRepository.findAll();
        List<CustomerDto> customerDtos = customers.stream()
                .map(customer -> bankAcountMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customerDtos;
    }

    public BankAccount getBankAccount(String accountId){
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Account "+accountId+" is not found"));
        return bankAccount;
    }
    @Override
    public BankAccountDto getBankAccountDto(String accountId) {
        BankAccount bankAccount = getBankAccount(accountId);
        if (bankAccount instanceof CurrentAccount) {
            return bankAcountMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        } else {
            return bankAcountMapper.fromSavingBankAccount((SavingAccount) bankAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) {
        BankAccount bankAccount = getBankAccount(accountId);
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficentException("Balance not sufficent");
        Operation operation = new Operation();
        operation.setType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDate(new Date());
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) {
        BankAccount bankAccount = getBankAccount(accountId);
        Operation operation = new Operation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDate(new Date());
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) {
        debit(accountIdSource,amount,"transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccountDto> BANK_ACCOUNT_LIST() {

        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        List<BankAccountDto> bankAccountDtos = bankAccountList.stream().map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount) {
                return bankAcountMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            } else {
                return bankAcountMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDtos;
    }

    @Override
    public List<OperationDto> getOperations() {
        List<Operation> operations = operationRepository.findAll();
        List<OperationDto> operationDtos = operations.stream().map(operation -> {
            return bankAcountMapper.fromOperation(operation);
        }).collect(Collectors.toList());
        return operationDtos;
    }

    @Override
    public List<OperationDto> accountHistory(String accountId) {
        List<Operation> operations = operationRepository.findByBankAccountId(accountId);
        return operations.stream()
                .map(operation -> bankAcountMapper.fromOperation(operation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDto getAccountHistory(String accountId, int page, int size) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFoundException("this account id: "+accountId+" is not found");
        Page<Operation> accountOperations = operationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
        List<OperationDto> operationDtos = accountOperations.getContent().stream().map(operation -> bankAcountMapper.fromOperation(operation)).collect(Collectors.toList());
        accountHistoryDto.setOperationDtos(operationDtos);
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDto;
    }
}
