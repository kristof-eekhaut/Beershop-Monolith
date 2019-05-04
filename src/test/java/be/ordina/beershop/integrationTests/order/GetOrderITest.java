package be.ordina.beershop.integrationTests.order;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.OrderTestData;
import be.ordina.beershop.product.ProductTestData;
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

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());
        Product westmalle = persistProduct(ProductTestData.westmalle().build());
        Order order = persistOrder(OrderTestData.uppaidOrder(customer, karmeliet, westmalle).build());

        mockMvc.perform(
                get("/orders/" + order.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId().toString()))
                .andExpect(jsonPath("$.customer.id").value(customer.getId().toString()))
                .andExpect(jsonPath("$.state").value("CREATED"))
                .andExpect(jsonPath("$.address.street").value("Koekoekstraat"))
                .andExpect(jsonPath("$.address.number").value("70"))
                .andExpect(jsonPath("$.address.postalCode").value("9090"))
                .andExpect(jsonPath("$.address.country").value("Belgium"))
                .andExpect(jsonPath("$.createdOn").isNotEmpty())
                .andExpect(jsonPath("$.shipmentId").isEmpty())

                // LineItem Karmeliet
                .andExpect(jsonPath("$.lineItems[0].id").isNotEmpty())
                .andExpect(jsonPath("$.lineItems[0].product.id").value(karmeliet.getId().toString()))
                .andExpect(jsonPath("$.lineItems[0].quantity").value(10))
                .andExpect(jsonPath("$.lineItems[0].price").value(12.0))

                // LineItem Westmalle
                .andExpect(jsonPath("$.lineItems[1].id").isNotEmpty())
                .andExpect(jsonPath("$.lineItems[1].product.id").value(westmalle.getId().toString()))
                .andExpect(jsonPath("$.lineItems[1].quantity").value(10))
                .andExpect(jsonPath("$.lineItems[1].price").value(13.0))

                .andExpect(jsonPath("$.shipmentId").isEmpty());
    }

    @Test
    public void whenOrderDoesNotExist_thenNotFound() throws Exception {
        mockMvc.perform(
                get("/orders/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}