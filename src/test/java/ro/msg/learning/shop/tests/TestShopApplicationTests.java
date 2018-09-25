package ro.msg.learning.shop.tests;

import org.flywaydb.core.Flyway;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestShopApplicationTests {

    @Autowired
    private Flyway flyway;

    //    @After
    public void resetDB() {
        flyway.clean();
        flyway.migrate();
    }
//    @Test
//    public void retrieveDetailsForCourse() throws Exception {
//
//        Mockito.when(
//            studentService.retrieveCourse(Mockito.anyString(),
//                Mockito.anyString())).thenReturn(mockCourse);
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
//            "/students/Student1/courses/Course1").accept(
//            MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//        System.out.println(result.getResponse());
//        String expected = "{id:Course1,name:Spring,description:10 Steps}";
//
//        // {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}
//
//        JSONAssert.assertEquals(expected, result.getResponse()
//            .getContentAsString(), false);
//    }

//    @Test(expected = )
//    public void singleStrategyNoResultsTest() throws Exception {
//
//
//        Mockito.when(
////            selectionStrategy.retrieveCourse(Mockito.anyString(),
////                Mockito.anyString())).thenReturn(mockCourse);
//            selectionStrategy.getStrategyResult(Mockito.any())
//        ).thenReturn(Collections.emptyList());
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
//            "/students/Student1/courses/Course1").accept(
//            MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//        System.out.println(result.getResponse());
//        String expected = "{id:Course1,name:Spring,description:10 Steps}";
//
//        // {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}
//
//        JSONAssert.assertEquals(expected, result.getResponse()
//            .getContentAsString(), false);
//    }

//    @Test
//    public void orderIsCorrectlyFormatedTest() {
//        OrderCreationService orderCreationServiceMock = Mockito.mock(OrderCreationService.class);
//
//        when(orderCreationServiceMock.createOrder(null)).thenReturn(null);
//
//        Assert.assertEquals(orderCreationServiceMock.createOrder(null), null);
//    }
//
//    @Test
//    public void orderAddressIncorrectTest() {
//        OrderCreationService orderCreationServiceMock = Mockito.mock(OrderCreationService.class);
//
//        OrderDto orderDto = new OrderDto();
//        orderDto.setAddress(new Address("Hungary", "Kosenom", "Titusko", "Sarma"));
//
//        try {
//            orderCreationServiceMock.createOrder(orderDto);
//            Assert.assertFalse(true);
//        } catch (Exception e) {
//            Assert.assertTrue(true);
//        }
//
//    }
//
//    @Test
//    public void singleLocationStrategyReceivesNullTest() {
//
//        try {
//            selectionStrategy.getStrategyResult(null);
//            Assert.assertFalse(true);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            Assert.assertTrue(true);
//        }
//
//    }

//    @Test
//    public void singleLocationStrategyFoundNoResults() {
//
//        try {
//
////            List<OrderDto> orderDtoList = new ArrayList<>(Arrays.asList(new OrderDto[]{
////                new OrderDto(
////                    LocalDateTime.now(),
////                    null,
////                    new ArrayList<>(Arrays.asList(new OrderDetailDto[]{new OrderDetailDto(113521, 2153135)}))
////            )}
//
////            List<OrderDetail> orderDetailList=new ArrayList<>(Arrays.asList(new OrderDetail[]{new OrderDetail(113521, 2153135)}))
////
////            selectionStrategy.getStrategyResult(orderDetailList);
//        }
//    }


}
