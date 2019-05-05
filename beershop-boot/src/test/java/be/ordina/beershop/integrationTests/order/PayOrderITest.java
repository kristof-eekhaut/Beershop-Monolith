package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.OrderStatus;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.OrderMatcher;
import be.ordina.beershop.order.OrderTestData;
import be.ordina.beershop.product.ProductTestData;
import be.ordina.beershop.shoppingcart.ShoppingCartTestData;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PayOrderITest extends IntegrationTest {

    @Test
    public void givenOrder_whenPayingOrder_thenOrderIsPaid() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCartTestData.cartWithItem(karmeliet).build())
                .build());

        Order order = persistOrder(OrderTestData.unpaidOrder(customer, karmeliet).build());

        mockMvc.perform(
                patch("/orders/" + order.getId() + "/pay"))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Order updatedOrder = orderRepository.findById(order.getId()).get();
            MatcherAssert.assertThat(updatedOrder, OrderMatcher.matchesOrder(OrderTestData.unpaidOrder(customer, karmeliet)
                    .state(OrderStatus.PAID)
                    .build()));
        });
    }
}
