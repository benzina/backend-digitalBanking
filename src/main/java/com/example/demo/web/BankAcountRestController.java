package com.example.demo.web;

import com.example.demo.dtos.AccountHistoryDto;
import com.example.demo.dtos.BankAccountDto;
import com.example.demo.dtos.OperationDto;
import com.example.demo.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class BankAcountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDto getBankAccount(@PathVariable String accountId){
        return bankAccountService.getBankAccountDto(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDto> getBankAccounts(){
        return bankAccountService.BANK_ACCOUNT_LIST();
    }

    @GetMapping("accounts/{accountId}/operations")
    public List<OperationDto> history(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("accounts/{accountId}/pageOperations")
    public AccountHistoryDto historyPages(@PathVariable String accountId,
                                          @RequestParam(name = "page",defaultValue = "0") int page,
                                          @RequestParam(name = "size",defaultValue = "5") int size){
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

}
