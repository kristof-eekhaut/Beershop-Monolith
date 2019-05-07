package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.OrderMatcher;
import be.ordina.beershop.order.OrderTestData;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.repository.entities.*;
import be.ordina.beershop.service.ShipmentResponseDto;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static be.ordina.beershop.shoppingcart.JPAShoppingCartTestData.cartWithItems;
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

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        Order order = persistOrder(OrderTestData.paidOrder(customer, shoppingCart.getId(), karmeliet).build());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment"))
                .andDo(print())
                .andExpect(status().isOk());

        mockServer.verify();

        runInTransaction(() -> {
            Order updatedOrder = orderRepository.findById(order.getId()).get();
            MatcherAssert.assertThat(updatedOrder, OrderMatcher.matchesOrder(OrderTestData.unpaidOrder(customer, shoppingCart.getId(), karmeliet)
                    .state(OrderStatus.SHIPMENT_REQUESTED)
                    .shipmentId("123")
                    .build()));
        });
    }

    @Test
    public void givenPaidOrderWithNotEnoughStock_whenShipmentIsRequested_thenOrderStateIsFailed() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet()
                .quantity(0)
                .build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        Order order = persistOrder(OrderTestData.paidOrder(customer, shoppingCart.getId(), karmeliet).build());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment"))
                .andDo(print())
                .andExpect(status().isOk());

        mockServer.verify();

        runInTransaction(() -> {
            Order updatedOrder = orderRepository.findById(order.getId()).get();
            MatcherAssert.assertThat(updatedOrder, OrderMatcher.matchesOrder(OrderTestData.unpaidOrder(customer, shoppingCart.getId(), karmeliet)
                    .state(OrderStatus.ORDER_FAILED)
                    .build()));
        });
    }

    @Test
    public void givenUnpaidOrder_whenShipmentIsRequested_thenError() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        Order order = persistOrder(OrderTestData.unpaidOrder(customer, shoppingCart.getId(), karmeliet).build());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment"))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        runInTransaction(() -> {
            Order updatedOrder = orderRepository.findById(order.getId()).get();
            MatcherAssert.assertThat(updatedOrder, OrderMatcher.matchesOrder(OrderTestData.unpaidOrder(customer, shoppingCart.getId(), karmeliet)
                    .state(OrderStatus.CREATED)
                    .build()));
        });
    }
}
