package com.example.demo.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDto {
    private String accountId;
    private double balance;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private List<OperationDto> operationDtos;
}
