package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.FileTypeMismatchException;
import ro.msg.learning.shop.services.ExportStocksService;
import ro.msg.learning.shop.services.OrderCreationService;
import ro.msg.learning.shop.services.ProductService;
import ro.msg.learning.shop.utilities.CsvConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Testing requests for now
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final CsvConverter csvConverter;
    private final ProductService productService;
    private final OrderCreationService orderCreationService;
    private final ExportStocksService exportStocksService;

    @GetMapping
    List<Product> productList() {
        return productService.getAllProducts();
    }

    @GetMapping("/requestParam")
    String requestParamExample(@RequestParam(name = "field", defaultValue = "defaultField") String field) {
        return (field);
    }

    @GetMapping("pathVariable/{id}")
    int pathVariableExample(@PathVariable int id) {
        return id;
    }

    @PostMapping("post/{description}/{name}/{price}/{weight}")
    void postProduct(@PathVariable String description, @PathVariable String name, @PathVariable Double price, @PathVariable Double weight) {
        productService.saveNewProduct(description, name, price, weight);
    }

    @PostMapping("/post")
    Product postProduct2(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    //testing order creation service

    @PostMapping("/bla")
    public String bla() {

        //TODO get orderDto instance from request body
      /*  OrderDto orderDto = new OrderDto();

        orderDto.setOrderTimestamp(LocalDateTime.now());
        orderDto.setAddress(new Address("Romania", "Arad", "Arad", "Clujului"));

        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        OrderDetailDto odd1 = new OrderDetailDto();
        odd1.setProductId(4);
        odd1.setQuantity(200);
        orderDetailDtoList.add(odd1);

        OrderDetailDto odd2 = new OrderDetailDto();
        odd2.setProductId(13); //forced it to have the same location
        odd2.setQuantity(300);
        orderDetailDtoList.add(odd2);

        orderDto.setOrderDetails(orderDetailDtoList);

        CsvConverter<OrderDto> csvConverter = new CsvConverter<>();
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderDtoList.add(orderDto);
        OutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            return csvConverter.toCsv( orderDto,OrderDto.class,orderDtoList,outputStream );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }*/
        return null;
//        return orderCreationService.createOrder(orderDto);
    }

    @PostMapping("/bla2")
    public List<Stock> bla2() {
        return exportStocksService.getAllStocksByLocationId(10);
    }

    @GetMapping(path = "/csv-try-write", produces = "text/csv")
    public List<OrderDetailDto> csvCreatedString() {

        OrderDetailDto orderDetailDto1 = new OrderDetailDto();
        orderDetailDto1.setQuantity(1234);
        orderDetailDto1.setProductId(12);

        OrderDetailDto orderDetailDto2 = new OrderDetailDto();
        orderDetailDto2.setQuantity(4321);
        orderDetailDto2.setProductId(21);

        OrderDetailDto orderDetailDto3 = new OrderDetailDto();
        orderDetailDto3.setQuantity(3333);
        orderDetailDto3.setProductId(33);

        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        orderDetailDtoList.add(orderDetailDto1);
        orderDetailDtoList.add(orderDetailDto2);
        orderDetailDtoList.add(orderDetailDto3);
/*
        OrderDetailDto plainOdd=new OrderDetailDto();

        OutputStream outputStream=new ByteArrayOutputStream();
        try {
            return csvConverter.toCsv(plainOdd, OrderDetailDto.class, orderDetailDtoList, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        return orderDetailDtoList;
    }

    @SneakyThrows
    @PostMapping(value = "/csv-from-file")
    public List<OrderDetailDto> csvFromFile(@RequestParam("file") MultipartFile file) {

        if (!file.getOriginalFilename().endsWith(".csv"))
            throw new FileTypeMismatchException("Expected: .csv file, received: " + file.getOriginalFilename());

        try {
            return csvConverter.fromCsv(OrderDetailDto.class, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping(path = "/csv-try-read", consumes = "text/csv")
    public List<OrderDetailDto> csvCreatedList(@RequestBody List<OrderDetailDto> orderDetailDtoList) {
        return orderDetailDtoList;
    }


}
