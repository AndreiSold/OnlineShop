package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.CustomerDto;
import ro.msg.learning.shop.dtos.CustomerDtoNoPassword;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.services.CustomerService;

import java.security.Principal;

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

    @GetMapping(value = "/profile")
    @ResponseBody
    public CustomerDtoNoPassword showCustomerProfile(Principal principal) {
        return customerService.customerDtoNoPasswordFromCustomerUsername(principal.getName());
    }

    @DeleteMapping(value = "/{idToDelete}")
    public void deleteCustomerAfterId(@PathVariable Integer idToDelete) {
        customerService.deleteCustomerById(idToDelete);
    }
}
