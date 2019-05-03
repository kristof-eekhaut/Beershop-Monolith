package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.OrderStatus;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.ProductTestData;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static be.ordina.beershop.common.AddressTestData.koekoekstraat70;
import static be.ordina.beershop.order.LineItemTestData.lineItem;
import static be.ordina.beershop.order.OrderMatcher.matchesOrder;
import static be.ordina.beershop.shoppingcart.ShoppingCartTestData.emptyCart;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateOrderITest extends IntegrationTest {

    @Test
    public void givenCustomerWithShoppingCart_whenCreatingNewOrder_thenOrderIsCreated() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());
        Product westmalle = persistProduct(ProductTestData.westmalle().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(emptyCart()
                        .lineItem(lineItem(karmeliet)
                                .quantity(5)
                                .build())
                        .lineItem(lineItem(westmalle)
                                .quantity(2)
                                .build())
                        .build())
                .build());

        final CreateOrderDTO createOrders = new CreateOrderDTO(customer.getId().toString());

        mockMvc.perform(
                post("/orders")
                        .content(objectMapper.writeValueAsString(createOrders))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        runInTransaction(() -> {
            List<Order> persistedOrders = orderRepository.findAll();
            assertThat(persistedOrders, hasSize(1));

            Order createdOrder = persistedOrders.get(0);
            assertThat(createdOrder, matchesOrder(Order.builder()
                    .id(createdOrder.getId())
                    .customer(customer)
                    .address(koekoekstraat70().build())
                    .state(OrderStatus.CREATED)
                    .lineItem(lineItem(karmeliet)
                            .quantity(5)
                            .build())
                    .lineItem(lineItem(westmalle)
                            .quantity(2)
                            .build())
                    .build()));
        });
    }

    @Test
    public void givenNoCustomer_whenCreatingNewOrder_thenOrderIsNotCreated() throws Exception {

        final CreateOrderDTO createOrders = new CreateOrderDTO(UUID.randomUUID().toString());

        mockMvc.perform(
                post("/orders")
                        .content(objectMapper.writeValueAsString(createOrders))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        runInTransaction(() -> {
            List<Order> persistedOrders = orderRepository.findAll();
            assertThat(persistedOrders, hasSize(0));
        });
    }
}