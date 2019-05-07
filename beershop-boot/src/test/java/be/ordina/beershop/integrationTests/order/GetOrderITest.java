package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.OrderTestData;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.Order;
import org.junit.Test;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetOrderITest extends IntegrationTest {

    @Test
    public void whenOrderIsFound_thenOrderIsReturned() throws Exception {

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        JPAProduct westmalle = persistProduct(JPAProductTestData.westmalle().build());
        Order order = persistOrder(OrderTestData.unpaidOrder(customer, UUID.randomUUID(), karmeliet, westmalle).build());

        mockMvc.perform(
                get("/orders/" + order.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId().toString()))
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.state").value("CREATED"))
                .andExpect(jsonPath("$.shipmentAddress.street").value("Koekoekstraat"))
                .andExpect(jsonPath("$.shipmentAddress.number").value("70"))
                .andExpect(jsonPath("$.shipmentAddress.postalCode").value("9090"))
                .andExpect(jsonPath("$.shipmentAddress.country").value("Belgium"))
                .andExpect(jsonPath("$.shipmentId").isEmpty())

                // Shopping cart item: Karmeliet
                .andExpect(jsonPath("$.lineItems[0].productId").value(karmeliet.getId().toString()))
                .andExpect(jsonPath("$.lineItems[0].quantity").value(10))
                .andExpect(jsonPath("$.lineItems[0].price").value(12.0))

                // Shopping cart item: Westmalle
                .andExpect(jsonPath("$.lineItems[1].productId").value(westmalle.getId().toString()))
                .andExpect(jsonPath("$.lineItems[1].quantity").value(10))
                .andExpect(jsonPath("$.lineItems[1].price").value(13.0));
    }

    @Test
    public void whenOrderDoesNotExist_thenNotFound() throws Exception {
        mockMvc.perform(
                get("/orders/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}