package be.ordina.beershop.integrationTests.shoppingcart;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.JPAShoppingCart;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static be.ordina.beershop.shoppingcart.JPAShoppingCartTestData.cartWithItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RemoveItemFromShoppingCartITest extends IntegrationTest {

    @Test
    void givenCustomerWithItemInShoppingCart_whenRemovingItem_thenShoppingCartIsEmpty() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/remove-product/" + karmeliet.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shoppingCart.getId().toString()))
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(0.00))

                .andExpect(jsonPath("$.items").value(hasSize(0)));
    }

    @Test
    void givenCustomerWithMultipleItemsInShoppingCart_whenRemovingItem_thenItemIsRemovedAndOthersStayInShoppingCart() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        JPAProduct westmalle = persistProduct(JPAProductTestData.westmalle().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet, westmalle).build());

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/remove-product/" + karmeliet.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shoppingCart.getId().toString()))
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(13.00))

                .andExpect(jsonPath("$.items").value(hasSize(1)))

                // Shopping cart item: Westmalle
                .andExpect(jsonPath("$.items[0].productId").value(westmalle.getId().toString()))
                .andExpect(jsonPath("$.items[0].quantity").value(10))
                .andExpect(jsonPath("$.items[0].productPrice").value(1.30))
                .andExpect(jsonPath("$.items[0].totalPrice").value(13.00));
    }

    @Test
    void givenCustomerWithItemInShoppingCart_whenRemovingNonExistingItem_thenShoppingCartIsUnchanged() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/remove-product/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shoppingCart.getId().toString()))
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(12.00))

                .andExpect(jsonPath("$.items").value(hasSize(1)))

                // Shopping cart item: Westmalle
                .andExpect(jsonPath("$.items[0].productId").value(karmeliet.getId().toString()))
                .andExpect(jsonPath("$.items[0].quantity").value(10))
                .andExpect(jsonPath("$.items[0].productPrice").value(1.20))
                .andExpect(jsonPath("$.items[0].totalPrice").value(12.00));
    }

    @Test
    void givenNoCustomer_whenRemovingItem_thenNotFound() throws Exception {

        mockMvc.perform(
                delete("/customers/" + UUID.randomUUID() + "/shopping-cart/line-items/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
