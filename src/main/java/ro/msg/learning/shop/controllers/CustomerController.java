package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.CustomerDto;
import ro.msg.learning.shop.dtos.CustomerDtoNoPassword;
import ro.msg.learning.shop.dtos.UserDto;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.services.CustomerService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customer/login")
    public boolean checkIfCredentialsAreCorrect(@RequestBody UserDto userDto) {
        return customerService.checkIfCredentialsAreCorrect(userDto.getUsername(), userDto.getPassword());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/customer/register")
    public Customer registerCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.registerCustomer(customerDto);
    }

    @GetMapping("/customer/isAdmin/{customerUsername}")
    public boolean isAdmin(@PathVariable String customerUsername) {
        return customerService.isAdmin(customerUsername);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/profile/{customerUsername}")
    public CustomerDtoNoPassword showCustomerProfile(@PathVariable String customerUsername) {
        return customerService.customerDtoNoPasswordFromCustomerUsername(customerUsername);
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomerById(@PathVariable Integer id) {
        customerService.deleteCustomerById(id);
    }
}
