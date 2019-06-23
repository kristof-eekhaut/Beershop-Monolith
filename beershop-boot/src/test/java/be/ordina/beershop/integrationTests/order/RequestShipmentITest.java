package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.JPAOrderMatcher;
import be.ordina.beershop.order.JPAOrderTestData;
import be.ordina.beershop.order.OrderStatus;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.JPAOrder;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.JPAShoppingCart;
import be.ordina.beershop.service.ShipmentResponseDto;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static be.ordina.beershop.order.JPAOrderTestData.unpaidOrder;
import static be.ordina.beershop.shoppingcart.JPAShoppingCartTestData.cartWithItems;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RequestShipmentITest extends IntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        if (mockServer == null) {
            this.mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
        }
        mockServer.reset();
    }

    @Test
    void givenPaidOrder_whenShipmentIsRequested_thenOrderStateIsShipmentRequested() throws Exception {

        mockServer.expect(requestTo("http://example.be/createShipment"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(new ShipmentResponseDto("123", "DELIVERED")), APPLICATION_JSON));

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        JPAOrder order = persistOrder(JPAOrderTestData.paidOrder(customer, shoppingCart.getId(), karmeliet).build());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment"))
                .andDo(print())
                .andExpect(status().isOk());

        mockServer.verify();

        runInTransaction(() -> {
            JPAOrder updatedOrder = orderRepository.findById(order.getId()).get();
            MatcherAssert.assertThat(updatedOrder, JPAOrderMatcher.matchesOrder(unpaidOrder(customer, shoppingCart.getId(), karmeliet)
                    .state(OrderStatus.SHIPMENT_REQUESTED)
                    .shipmentTrackingNumber("123")
                    .build()));
        });
    }

    @Test
    void givenPaidOrderWithNotEnoughStock_whenShipmentIsRequested_thenOrderStateIsFailed() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet()
                .quantity(0)
                .build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        JPAOrder order = persistOrder(JPAOrderTestData.paidOrder(customer, shoppingCart.getId(), karmeliet).build());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment"))
                .andDo(print())
                .andExpect(status().isOk());

        mockServer.verify();

        runInTransaction(() -> {
            JPAOrder updatedOrder = orderRepository.findById(order.getId()).get();
            MatcherAssert.assertThat(updatedOrder, JPAOrderMatcher.matchesOrder(unpaidOrder(customer, shoppingCart.getId(), karmeliet)
                    .state(OrderStatus.ORDER_FAILED)
                    .build()));
        });
    }

    @Test
    void givenUnpaidOrder_whenShipmentIsRequested_thenError() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        JPAOrder order = persistOrder(unpaidOrder(customer, shoppingCart.getId(), karmeliet).build());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/requestShipment"))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        runInTransaction(() -> {
            JPAOrder updatedOrder = orderRepository.findById(order.getId()).get();
            MatcherAssert.assertThat(updatedOrder, JPAOrderMatcher.matchesOrder(unpaidOrder(customer, shoppingCart.getId(), karmeliet)
                    .state(OrderStatus.CREATED)
                    .build()));
        });
    }
}
