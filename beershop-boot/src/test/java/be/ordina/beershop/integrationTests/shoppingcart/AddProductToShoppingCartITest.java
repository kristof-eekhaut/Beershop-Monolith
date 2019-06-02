package be.ordina.beershop.integrationTests.shoppingcart;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.JPAShoppingCart;
import be.ordina.beershop.shoppingcart.AddProductToShoppingCart;
import be.ordina.beershop.shoppingcart.JPAShoppingCartTestData;
import be.ordina.beershop.shoppingcart.exception.ShoppingCartErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static be.ordina.beershop.shoppingcart.JPAShoppingCartTestData.cartWithItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AddProductToShoppingCartITest extends IntegrationTest {

    @Test
    void givenCustomerWithEmptyShoppingCart_whenAddingItemToShoppingCart_thenShoppingCartContainsItem() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(karmeliet.getId().toString(), 5);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
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
    void givenCustomerWithItemInShoppingCart_whenAddingOtherItemToShoppingCart_thenShoppingCartContains2Items() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        JPAProduct westmalle = persistProduct(JPAProductTestData.westmalle().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(cartWithItems(customer.getId(), karmeliet).build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(westmalle.getId().toString(), 4);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shoppingCart.getId().toString()))
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(17.20))

                .andExpect(jsonPath("$.items").value(hasSize(2)))

                // Shopping cart item: Karmeliet
                .andExpect(jsonPath("$.items[0].productId").value(karmeliet.getId().toString()))
                .andExpect(jsonPath("$.items[0].quantity").value(10))
                .andExpect(jsonPath("$.items[0].productPrice").value(1.20))
                .andExpect(jsonPath("$.items[0].totalPrice").value(12.00))

                // Shopping cart item: Westmalle
                .andExpect(jsonPath("$.items[1].productId").value(westmalle.getId().toString()))
                .andExpect(jsonPath("$.items[1].quantity").value(4))
                .andExpect(jsonPath("$.items[1].productPrice").value(1.30))
                .andExpect(jsonPath("$.items[1].totalPrice").value(5.20));
    }

    @Test
    void givenCustomerWithItemInShoppingCart_whenAddingMoreOfTheSameItemToShoppingCart_thenShoppingCartQuantityIsIncreased() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());
        JPAShoppingCart shoppingCart = persistShoppingCart(JPAShoppingCartTestData.cartWithItems(customer.getId(), karmeliet).build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(karmeliet.getId().toString(), 4);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shoppingCart.getId().toString()))
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(16.80))

                .andExpect(jsonPath("$.items").value(hasSize(1)))

                // Shopping cart item: Karmeliet
                .andExpect(jsonPath("$.items[0].productId").value(karmeliet.getId().toString()))
                .andExpect(jsonPath("$.items[0].quantity").value(14))
                .andExpect(jsonPath("$.items[0].productPrice").value(1.20))
                .andExpect(jsonPath("$.items[0].totalPrice").value(16.80));
    }

    @Test
    void givenCustomerWithEmptyShoppingCart_whenAddingItemWithQuantity0ToShoppingCart_thenShoppingCartIsNotUpdated() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(karmeliet.getId().toString(), 0);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ShoppingCartErrorCode.INVALID_SHOPPING_CART_ITEM_QUANTITY.getCode()));

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(0.00))

                .andExpect(jsonPath("$.items").value(hasSize(0)));
    }

    @Test
    void givenNoCustomer_whenAddingItemToShoppingCart_thenNotFound() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(karmeliet.getId().toString(), 5);

        mockMvc.perform(
                patch("/customers/" + UUID.randomUUID() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenCustomerWithEmptyShoppingCart_whenAddingNonExistingItemToShoppingCart_thenShoppingCartIsNotUpdated() throws Exception {

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(UUID.randomUUID().toString(), 5);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ShoppingCartErrorCode.PRODUCT_NOT_FOUND.getCode()));

        mockMvc.perform(
                get("/customers/" + customer.getId() + "/shopping-cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.customerId").value(customer.getId().toString()))
                .andExpect(jsonPath("$.totalPrice").value(0.00))

                .andExpect(jsonPath("$.items").value(hasSize(0)));
    }
}
