package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.CustomerDto;
import ro.msg.learning.shop.dtos.CustomerDtoNoPassword;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.services.CustomerService;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register")
    public Customer registerCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.registerCustomer(customerDto);
    }

    @GetMapping(value = "/profile/{customer}")
    public CustomerDtoNoPassword showCustomerProfile(@PathVariable("customer") Customer customer) {
        return customerService.customerDtoNoPasswordFromCustomer(customer);
    }

    @GetMapping(value = "/profile")
    @ResponseBody
    public CustomerDtoNoPassword showCustomerProfile(Authentication authentication) {
        return customerService.customerDtoNoPasswordFromCustomerUsername(authentication.getName());
    }
}
