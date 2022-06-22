package com.example.demo.dtos;

import com.example.demo.entities.BankAccount;
import com.example.demo.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
public class OperationDto {
    private Long id;
    private Date date;
    private double amount;
    private String description;
    private OperationType type;
}
