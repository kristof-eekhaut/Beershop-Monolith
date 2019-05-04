package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.OrderStatus;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.OrderTestData;
import be.ordina.beershop.product.ProductTestData;
import org.junit.Test;
import org.springframework.http.MediaType;

import static be.ordina.beershop.order.OrderMatcher.matchesOrder;
import static be.ordina.beershop.shoppingcart.ShoppingCartTestData.cartWithItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PayOrderITest extends IntegrationTest {

    @Test
    public void givenOrder_whenPayingOrder_thenOrderIsPaid() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(cartWithItem(karmeliet).build())
                .build());

        Order order = persistOrder(OrderTestData.uppaidOrder(customer, karmeliet).build());

        final PayOrderDTO payOrderDTO = new PayOrderDTO(order.getId().toString());

        mockMvc.perform(
                post("/payments")
                        .content(objectMapper.writeValueAsString(payOrderDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Order updatedOrder = orderRepository.findById(order.getId()).get();
            assertThat(updatedOrder, matchesOrder(OrderTestData.uppaidOrder(customer, karmeliet)
                    .state(OrderStatus.PAID)
                    .build()));
        });
    }
}
