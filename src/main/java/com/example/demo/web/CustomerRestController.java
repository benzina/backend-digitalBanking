package com.example.demo.web;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.entities.Customer;
import com.example.demo.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDto> customers(){
        return bankAccountService.listCustomers();
    }

    @GetMapping("customers/{id}")
    public CustomerDto getCustomer(@PathVariable(name = "id") Long id){
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/customers")
    public CustomerDto saveCustomer(@RequestBody CustomerDto customerDto){
        return bankAccountService.saveCustomer(customerDto);
    }

    @PutMapping("/customers/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id,@RequestBody CustomerDto customerDto){
        customerDto.setId(id);
        return bankAccountService.updateCustomer(customerDto);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }
}
