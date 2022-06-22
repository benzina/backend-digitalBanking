package com.example.demo.dtos;

import com.example.demo.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentBankAccountDto extends BankAccountDto{
        private String id;
        private Date createdAt;
        private double balance;
        private AccountStatus status;
        private String currency;
        private CustomerDto customerDto;
        private double overDraft;
}
