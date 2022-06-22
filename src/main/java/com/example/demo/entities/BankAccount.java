package com.example.demo.entities;

import com.example.demo.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "bankAccountType",length = 2,discriminatorType = DiscriminatorType.STRING)
@Data @NoArgsConstructor @AllArgsConstructor
public class BankAccount {
    @Id
    private String id;
    private Date createdAt;
    private double balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount",fetch = FetchType.LAZY)
    private List<Operation> operations;
}
