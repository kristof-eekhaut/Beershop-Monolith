package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.OrderStatus;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.OrderTestData;
import be.ordina.beershop.product.ProductTestData;
import be.ordina.beershop.service.ShipmentResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static be.ordina.beershop.order.OrderMatcher.matchesOrder;
import static be.ordina.beershop.shoppingcart.ShoppingCartTestData.cartWithItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RequestShipmentITest extends IntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        if (mockServer == null) {
            this.mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
        }
        mockServer.reset();
    }

    @Test
    public void givenPaidOrder_whenShipmentIsRequested_thenOrderStateIsShipmentRequested() throws Exception {

        mockServer.expect(requestTo("http://example.be/createShipment"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(new ShipmentResponseDto("123", "DELIVERED")), APPLICATION_JSON));

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(cartWithItem(karmeliet).build())
                .build());

        Order order = persistOrder(OrderTestData.paidOrder(customer, karmeliet).build());

        final RequestShipmentDTO requestShipmentDTO = new RequestShipmentDTO(order.getId().toString());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment")
                        .content(objectMapper.writeValueAsString(requestShipmentDTO))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockServer.verify();

        runInTransaction(() -> {
            Order updatedOrder = orderRepository.findById(order.getId()).get();
            assertThat(updatedOrder, matchesOrder(OrderTestData.unpaidOrder(customer, karmeliet)
                    .state(OrderStatus.SHIPMENT_REQUESTED)
                    .shipmentId("123")
                    .build()));
        });
    }

    @Test
    public void givenPaidOrderWithNotEnoughStock_whenShipmentIsRequested_thenOrderStateIsFailed() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet()
                .quantity(0)
                .build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(cartWithItem(karmeliet).build())
                .build());

        Order order = persistOrder(OrderTestData.paidOrder(customer, karmeliet).build());

        final RequestShipmentDTO requestShipmentDTO = new RequestShipmentDTO(order.getId().toString());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment")
                        .content(objectMapper.writeValueAsString(requestShipmentDTO))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockServer.verify();

        runInTransaction(() -> {
            Order updatedOrder = orderRepository.findById(order.getId()).get();
            assertThat(updatedOrder, matchesOrder(OrderTestData.unpaidOrder(customer, karmeliet)
                    .state(OrderStatus.ORDER_FAILED)
                    .build()));
        });
    }

    @Test
    public void givenUnpaidOrder_whenShipmentIsRequested_thenError() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(cartWithItem(karmeliet).build())
                .build());

        Order order = persistOrder(OrderTestData.unpaidOrder(customer, karmeliet).build());

        final RequestShipmentDTO requestShipmentDTO = new RequestShipmentDTO(order.getId().toString());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment")
                        .content(objectMapper.writeValueAsString(requestShipmentDTO))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        runInTransaction(() -> {
            Order updatedOrder = orderRepository.findById(order.getId()).get();
            assertThat(updatedOrder, matchesOrder(OrderTestData.unpaidOrder(customer, karmeliet)
                    .state(OrderStatus.CREATED)
                    .build()));
        });
    }
}
