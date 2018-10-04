package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.CustomerDto;
import ro.msg.learning.shop.dtos.CustomerDtoNoPassword;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.exceptions.CustomerIdNotFoundException;
import ro.msg.learning.shop.repositories.CustomerRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public Customer registerCustomer(CustomerDto customerDto) {
        return customerRepository.save(Customer.builder().firstName(customerDto.getFirstName())
            .lastName(customerDto.getLastName())
            .username(customerDto.getUsername())
            .roles(customerDto.getRoles())
            .password(passwordEncoder.encode(customerDto.getPassword()))
            .build());
    }

    public CustomerDtoNoPassword customerDtoNoPasswordFromCustomer(Customer customer) {
        return CustomerDtoNoPassword.builder().firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .orders(customer.getOrders())
            .roles(customer.getRoles())
            .username(customer.getUsername())
            .build();
    }

    public CustomerDtoNoPassword customerDtoNoPasswordFromCustomerUsername(String username) {
        Customer customer = customerRepository.findByUsernameEquals(username);

        if (customer == null) {
            log.error("Customer username not found!");
            throw new CustomerIdNotFoundException("Username does not exist!");
        }

        return CustomerDtoNoPassword.builder().firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .orders(customer.getOrders())
            .roles(customer.getRoles())
            .username(customer.getUsername())
            .build();
    }
}
