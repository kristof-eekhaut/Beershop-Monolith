package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.domain.AddressTestData;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.CreateOrder;
import be.ordina.beershop.order.OrderMatcher;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.repository.entities.*;
import be.ordina.beershop.shoppingcart.JPAShoppingCartTestData;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static be.ordina.beershop.order.JPAShoppingCartItemTestData.shoppingCartItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateOrderITest extends IntegrationTest {

    @Test
    void givenCustomerWithShoppingCart_whenCreatingNewOrder_thenOrderIsCreatedAndShoppingCartIsCleared() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        JPAProduct westmalle = persistProduct(JPAProductTestData.westmalle().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        UUID shoppingCartId = UUID.randomUUID();
        JPAShoppingCart shoppingCart = persistShoppingCart(JPAShoppingCartTestData.emptyCart(customer.getId())
                .id(shoppingCartId)
                .item(shoppingCartItem(shoppingCartId, karmeliet)
                        .quantity(5)
                        .build())
                .item(shoppingCartItem(shoppingCartId, westmalle)
                        .quantity(2)
                        .build())
                .build());

        final CreateOrder createOrders = new CreateOrder(customer.getId().toString());

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
            MatcherAssert.assertThat(createdOrder, OrderMatcher.matchesOrder(Order.builder()
                    .id(createdOrder.getId())
                    .customer(customer)
                    .address(AddressTestData.koekoekstraat70().build())
                    .state(OrderStatus.CREATED)
                    .lineItem(shoppingCartItem(shoppingCartId, karmeliet)
                            .quantity(5)
                            .build())
                    .lineItem(shoppingCartItem(shoppingCartId, westmalle)
                            .quantity(2)
                            .build())
                    .build()));

            JPAShoppingCart updatedShoppingCart = jpaShoppingCartDAO.findByCustomerId(customer.getId()).get();
            assertThat(updatedShoppingCart.getItems(), hasSize(0));
        });
    }

    @Test
    void givenNoCustomer_whenCreatingNewOrder_thenOrderIsNotCreated() throws Exception {

        final CreateOrder createOrders = new CreateOrder(UUID.randomUUID().toString());

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