package ro.msg.learning.shop.tests.utilities;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.utilities.CsvConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CsvConverterTest {

    @Autowired
    private CsvConverter csvConverter;

    @Test
    public void toCsvTest() {
        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>(Arrays.asList(
            new OrderDetailDto(1, 100),
            new OrderDetailDto(2, 200),
            new OrderDetailDto(3, 300)
        ));

        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            csvConverter.toCsv(OrderDetailDto.class, orderDetailDtoList, outputStream);

            String result = outputStream.toString();

            Assert.assertEquals(result,
                "productId,quantity\n" +
                    "1,100\n" +
                    "2,200\n" +
                    "3,300\n");
        } catch (IOException e) {
            log.error("IOException hit while trying to test toCsv!", e);
        }
    }

    @Test
    public void fromCsvTest() {
        InputStream inputStream = new ByteArrayInputStream(("productId,quantity\n" +
            "1,100\n" +
            "2,200\n" +
            "3,300\n").getBytes());

        try {
            List<OrderDetailDto> resultList = csvConverter.fromCsv(OrderDetailDto.class, inputStream);

            List<OrderDetailDto> orderDetailDtoList = new ArrayList<>(Arrays.asList(
                new OrderDetailDto(1, 100),
                new OrderDetailDto(2, 200),
                new OrderDetailDto(3, 300)
            ));

            resultList.stream().forEach(orderDetailDto -> {
                int position = resultList.indexOf(orderDetailDto);

                Assert.assertEquals(orderDetailDtoList.get(position), orderDetailDto);
            });
        } catch (IOException e) {
            log.error("IOException hit while trying to test fromCsv!", e);
        }
    }
}