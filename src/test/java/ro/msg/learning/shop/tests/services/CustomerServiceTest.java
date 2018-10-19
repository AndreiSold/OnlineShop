package ro.msg.learning.shop.tests.services;

import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.CustomerDto;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.services.CustomerService;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class CustomerServiceTest {


    @Autowired
    private CustomerService customerService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Flyway flyway;

    @After
    public void reset() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void registerCustomerTest() {

        CustomerDto customerDto = CustomerDto.builder().username("vlad")
            .lastName("sar")
            .firstName("dar")
            .password("cascaPass")
            .build();

        Customer createdCustomer = customerService.registerCustomer(customerDto);

        Assert.assertEquals(customerDto.getFirstName(), createdCustomer.getFirstName());
        Assert.assertEquals(customerDto.getLastName(), createdCustomer.getLastName());
        Assert.assertEquals(customerDto.getUsername(), createdCustomer.getUsername());
        Assert.assertTrue(passwordEncoder.matches("cascaPass", createdCustomer.getPassword()));

        Customer customerInDb = customerRepository.findByUsername("vlad");

        Assert.assertNotNull(customerInDb);
    }

    @Test
    public void deleteCustomerByIdTest() {

        int idToDelete = 5;

        customerRepository.deleteById(idToDelete);

        Optional<Customer> customerOptional = customerRepository.findById(5);

        Assert.assertTrue(!customerOptional.isPresent());
    }
}