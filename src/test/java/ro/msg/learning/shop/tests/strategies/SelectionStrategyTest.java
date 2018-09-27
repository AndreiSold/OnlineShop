package ro.msg.learning.shop.tests.strategies;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.strategies.MultipleLocationsStrategy;
import ro.msg.learning.shop.strategies.SelectionStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SelectionStrategyTest {

    @Test
    public void singleLocationIsASelectionStrategyTest() {
        SingleLocationStrategy singleLocationStrategy = new SingleLocationStrategy();
        Assert.assertTrue(singleLocationStrategy instanceof SelectionStrategy);
    }

    @Test
    public void multipleLocationIsASelectionStrategyTest() {
        MultipleLocationsStrategy multipleLocationsStrategy = new MultipleLocationsStrategy();
        Assert.assertTrue(multipleLocationsStrategy instanceof SelectionStrategy);
    }

}