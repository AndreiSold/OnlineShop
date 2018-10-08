package ro.msg.learning.shop.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.CustomerDto;
import ro.msg.learning.shop.entities.Customer;

@Component
@Slf4j
public class CustomerDtoMapper {

    public CustomerDto customerToCustomerDto(Customer customer) {

        return CustomerDto.builder().username(customer.getUsername())
            .lastName(customer.getLastName())
            .firstName(customer.getFirstName())
            .build();
    }
}
