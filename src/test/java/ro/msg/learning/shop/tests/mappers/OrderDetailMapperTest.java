package ro.msg.learning.shop.tests.mappers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.mappers.OrderDetailMapper;

import java.util.ArrayList;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailMapperTest {

    @Autowired
    private OrderDetailMapper orderDetailMapperMock;

    @Test
    public void emptyListAsParameterTest() {
        Assert.assertEquals(Collections.emptyList(), orderDetailMapperMock.orderDetailDtoListToOrderDetailList(new ArrayList<>()));
    }

    @Test(expected = NullPointerException.class)
    public void nullListAsParameterTest() {
        orderDetailMapperMock.orderDetailDtoListToOrderDetailList(null);
    }

}