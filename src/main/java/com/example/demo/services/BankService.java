package com.example.demo.services;

import com.example.demo.entities.BankAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.SavingAccount;
import com.example.demo.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("164d9a88-4a4d-44d0-9493-1cb1d08bca5a").orElse(null);
        if (bankAccount != null){
            System.out.println("**************************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCurrency());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount){
                System.out.println("Over Draft => "+ ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount){
                System.out.println("Rate => "+ ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getOperations().forEach(operation -> {
                System.out.println(operation.getType() + "\t"+ operation.getDate() + "\t"+ operation.getAmount());
            });
        }

    }

}
