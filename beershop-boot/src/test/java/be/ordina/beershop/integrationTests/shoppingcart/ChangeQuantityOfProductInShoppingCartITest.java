package be.ordina.beershop.integrationTests.shoppingcart;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.JPAShoppingCart;
import be.ordina.beershop.shoppingcart.ChangeQuantityOfProductInShoppingCart;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static be.ordina.beershop.shoppingcart.JPAShoppingCartTestData.cartWithItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChangeQuantityOfProductInShoppingCartITest extends IntegrationTest {

    @Test
    public void givenCustomerWithItemInShoppingCart_whenChangingQuantity_thenItemQuantityIsUpdated() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        final ChangeQuantityOfProductInShoppingCart changeQuantityOfProductInShoppingCart = new ChangeQuantityOfProductInShoppingCart(karmeliet.getId().toString(), 5);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/change-quantity")
                        .content(objectMapper.writeValueAsString(changeQuantityOfProductInShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shoppingCart.getId().toString()))
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(6.00))

                .andExpect(jsonPath("$.items").value(hasSize(1)))

                // Shopping cart item: Karmeliet
                .andExpect(jsonPath("$.items[0].productId").value(karmeliet.getId().toString()))
                .andExpect(jsonPath("$.items[0].quantity").value(5))
                .andExpect(jsonPath("$.items[0].productPrice").value(1.20))
                .andExpect(jsonPath("$.items[0].totalPrice").value(6.00));
    }

    @Test
    public void givenNoCustomer_whenChangingQuantity_thenNotFound() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        final ChangeQuantityOfProductInShoppingCart changeQuantityOfProductInShoppingCart = new ChangeQuantityOfProductInShoppingCart(karmeliet.getId().toString(), 5);

        mockMvc.perform(
                patch("/customers/" + UUID.randomUUID() + "/shopping-cart/change-quantity")
                        .content(objectMapper.writeValueAsString(changeQuantityOfProductInShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
