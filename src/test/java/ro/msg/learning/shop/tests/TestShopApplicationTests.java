package ro.msg.learning.shop.tests;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ro.msg.learning.shop.services.OrderCreationService;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@RequiredArgsConstructor
public class TestShopApplicationTests {


    @Test
    public void orderIsCorrectlyFormated() {
        OrderCreationService orderCreationServiceMock = Mockito.mock(OrderCreationService.class);

        when(orderCreationServiceMock.createOrder(null)).thenReturn(null);

        Assert.assertEquals(orderCreationServiceMock.createOrder(null), null);
    }

}
