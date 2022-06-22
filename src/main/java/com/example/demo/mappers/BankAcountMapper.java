package com.example.demo.mappers;

import com.example.demo.dtos.CurrentBankAccountDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.OperationDto;
import com.example.demo.dtos.SavingBankAccountDto;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Operation;
import com.example.demo.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAcountMapper {
    public CustomerDto fromCustomer(Customer customer){
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer,customerDto);
     /*   customerDto.setId(customer.getId());
        customerDto.setEmail(customer.getEmail());
        customerDto.setName(customerDto.getName());
      */
        return customerDto;
    }

    public Customer toCustomer(CustomerDto customerDto){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto,customer);
        return customer;
    }

    public CurrentBankAccountDto fromCurrentBankAccount(CurrentAccount currentAccount){
        CurrentBankAccountDto currentBankAccountDto = new CurrentBankAccountDto();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDto);
        currentBankAccountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDto.setType(currentAccount.getClass().getSimpleName());
        return  currentBankAccountDto;
    }

    public CurrentAccount toCurrentBankAccount(CurrentBankAccountDto currentBankAccountDto){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDto,currentAccount);
        currentAccount.setCustomer(toCustomer(currentBankAccountDto.getCustomerDto()));
        return currentAccount;
    }

    public SavingBankAccountDto fromSavingBankAccount(SavingAccount savingAccount){
        SavingBankAccountDto savingBankAccountDto = new SavingBankAccountDto();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDto);
        savingBankAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDto.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDto;
    }

    public SavingAccount toSavingBankAccount(SavingBankAccountDto savingBankAccountDto){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDto,savingAccount);
        savingAccount.setCustomer(toCustomer(savingBankAccountDto.getCustomerDto()));
        return savingAccount;
    }

    public OperationDto fromOperation(Operation operation){
        OperationDto operationDto = new OperationDto();
        BeanUtils.copyProperties(operation,operationDto);
        return operationDto;
    }

    public Operation toOperation(OperationDto operationDto) {
        Operation operation = new Operation();
        BeanUtils.copyProperties(operationDto,operation);
        return operation;
    }

}
