package com.example.demo;

import com.example.demo.dtos.BankAccountDto;
import com.example.demo.dtos.CurrentBankAccountDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.SavingBankAccountDto;
import com.example.demo.entities.*;
import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.OperationType;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.OperationRepository;
import com.example.demo.services.BankAccountService;
import com.example.demo.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
		return args -> {
			Stream.of("hassan","imane","mohamed").forEach(name->{
				CustomerDto customer = new CustomerDto();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomers().forEach(customer -> {
				bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000, customer.getId());
				bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5, customer.getId());
			});
			List<BankAccountDto> bankAccounts = bankAccountService.BANK_ACCOUNT_LIST();
			for (BankAccountDto bankAccount:bankAccounts){
				for (int i=0; i<10; i++){
					String accountId;
					if (bankAccount instanceof SavingBankAccountDto) {
						accountId = ((SavingBankAccountDto) bankAccount).getId();
					} else {
						accountId = ((CurrentBankAccountDto) bankAccount).getId();
					}
					bankAccountService.credit(accountId,10000 + Math.random()*120000,"credit");
					bankAccountService.debit(accountId,1000 + Math.random()*9000,"debit");
				}
			}
		};
	}
	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							OperationRepository operationRepository){
		return args -> {
			Stream.of("hassan","yassine","aicha").forEach(name->{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});

			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setCustomer(customer);
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);
			});

			customerRepository.findAll().forEach(customer -> {
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setCustomer(customer);
				savingAccount.setBalance(Math.random()*90000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);
			});

			bankAccountRepository.findAll().forEach(bankAccount -> {
				for (int i=0; i<10;i++){
					Operation operation = new Operation();
					operation.setAmount(Math.random()*12000);
					operation.setDate(new Date());
					operation.setBankAccount(bankAccount);
					operation.setType(Math.random() > 0.5 ? OperationType.CREDIT:OperationType.DEBIT);
					operationRepository.save(operation);
				}
			});
		};
	}
}
