package ro.msg.learning.shop.tests.controllers;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.CustomerDto;
import ro.msg.learning.shop.dtos.CustomerDtoNoPassword;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Role;
import ro.msg.learning.shop.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class CustomerControllerTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private int port;

    private String basePath;
    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    private Flyway flyway;

    private void resetDB() {
        flyway.clean();
        flyway.migrate();
    }

    @Before
    public void init() {
        basePath = "http://localhost:" + port;
    }

    @Test
    public void registerTest() {

        String finalPath = basePath + "/customer/register";

        CustomerDto customerDto = CustomerDto.builder()
            .firstName("Andrei")
            .lastName("Alex")
            .username("AndreiAlex")
            .password("blana")
            .build();

        HttpEntity<CustomerDto> httpEntity = new HttpEntity<>(customerDto);

        ResponseEntity<Customer> responseEntity = testRestTemplate.postForEntity(finalPath, httpEntity, Customer.class);

        Customer createdCustomer = responseEntity.getBody();

        Customer customerInDb = customerRepository.findByUsername(createdCustomer.getUsername());

        Assert.assertEquals(createdCustomer.getFirstName(), customerInDb.getFirstName());
        Assert.assertEquals(createdCustomer.getLastName(), customerInDb.getLastName());
        Assert.assertTrue(passwordEncoder.matches("blana", customerInDb.getPassword()));
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        customerRepository.delete(createdCustomer);
    }

    @Test
    public void profileViewForAdminTest() {

        String finalPath = basePath + "/profile";

        ResponseEntity<CustomerDtoNoPassword> response = testRestTemplate.withBasicAuth("admin", "1234").getForEntity(finalPath, CustomerDtoNoPassword.class);

        CustomerDtoNoPassword createdCustomerDtoNoPassword = response.getBody();

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("admin", createdCustomerDtoNoPassword.getFirstName());
        Assert.assertEquals("admin", createdCustomerDtoNoPassword.getLastName());
        Assert.assertEquals("admin", createdCustomerDtoNoPassword.getUsername());

        List<Role> roleList = createdCustomerDtoNoPassword.getRoles();

        boolean isAdmin = false;
        for (Role role : roleList) {
            if (role.getName().equals("ROLE_ADMINISTRATOR")) {
                isAdmin = true;
            }
        }
        Assert.assertEquals(true, isAdmin);
    }

    @Test
    public void profileViewForNormalCustomerTest() {

        String finalPath = basePath + "/profile";

        ResponseEntity<CustomerDtoNoPassword> response = testRestTemplate.withBasicAuth("andreiSold", "1234").getForEntity(finalPath, CustomerDtoNoPassword.class);

        CustomerDtoNoPassword createdCustomerDtoNoPassword = response.getBody();

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Andrei", createdCustomerDtoNoPassword.getFirstName());
        Assert.assertEquals("Sold", createdCustomerDtoNoPassword.getLastName());
        Assert.assertEquals("andreiSold", createdCustomerDtoNoPassword.getUsername());

        List<Role> roleList = createdCustomerDtoNoPassword.getRoles();

        boolean isNotAdmin = true;
        for (Role role : roleList) {
            if (role.getName().equals("ROLE_ADMINISTRATOR")) {
                isNotAdmin = false;
            }
        }
        Assert.assertTrue(isNotAdmin);
    }

    @Test
    public void customerDeleteTest() {
        String finalPath = basePath + "/customer/1";

        testRestTemplate.withBasicAuth("admin", "1234").delete(finalPath);

        Optional<Customer> customer = customerRepository.findById(1);

        Assert.assertTrue(!customer.isPresent());

        resetDB();
    }
}
