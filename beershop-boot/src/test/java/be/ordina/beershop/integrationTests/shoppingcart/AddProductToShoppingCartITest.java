package be.ordina.beershop.integrationTests.shoppingcart;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.LineItem;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.ShoppingCart;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.LineItemMatcher;
import be.ordina.beershop.order.LineItemTestData;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.shoppingcart.AddProductToShoppingCart;
import be.ordina.beershop.shoppingcart.ShoppingCartErrorCode;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddProductToShoppingCartITest extends IntegrationTest {

    @Test
    public void givenCustomerWithEmptyShoppingCart_whenAddingItemToShoppingCart_thenShoppingCartContainsItem() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(karmeliet.getId().toString(), 5);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), hasSize(1));
            assertThat(shoppingCart.getLineItems().get(0),
                    LineItemMatcher.matchesLineItem(LineItem.builder()
                            .product(karmeliet)
                            .quantity(5)
                            .price(new BigDecimal("6.00"))
                            .build())
            );
        });
    }

    @Test
    public void givenCustomerWithItemInShoppingCart_whenAddingOtherItemToShoppingCart_thenShoppingCartContains2Items() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        JPAProduct westmalle = persistProduct(JPAProductTestData.westmalle().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCart.builder()
                        .id(UUID.randomUUID())
                        .lineItem(LineItemTestData.lineItem(karmeliet).build())
                        .build())
                .build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(westmalle.getId().toString(), 4);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), Matchers.containsInAnyOrder(
                    LineItemMatcher.matchesLineItem(LineItemTestData.lineItem(karmeliet).build()),
                    LineItemMatcher.matchesLineItem(LineItem.builder()
                            .product(westmalle)
                            .quantity(4)
                            .price(new BigDecimal("5.20"))
                            .build())
            ));
        });
    }

    @Test
    public void givenCustomerWithItemInShoppingCart_whenAddingMoreOfTheSameItemToShoppingCart_thenShoppingCartQuantityIsIncreased() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCart.builder()
                        .id(UUID.randomUUID())
                        .lineItem(LineItemTestData.lineItem(karmeliet).build())
                        .build())
                .build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(karmeliet.getId().toString(), 4);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            // TODO: these should be added together into 1 LineItem
            assertThat(shoppingCart.getLineItems(), Matchers.containsInAnyOrder(
                    LineItemMatcher.matchesLineItem(LineItemTestData.lineItem(karmeliet).build()),
                    LineItemMatcher.matchesLineItem(LineItem.builder()
                            .product(karmeliet)
                            .quantity(4)
                            .price(new BigDecimal("4.80"))
                            .build())
            ));
        });
    }

    @Test
    public void givenCustomerWithEmptyShoppingCart_whenAddingItemWithQuantity0ToShoppingCart_thenShoppingCartIsNotUpdated() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(karmeliet.getId().toString(), 0);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
//                .andExpect(status().isBadRequest()); // TODO: should be BAD_REQUEST
                .andExpect(status().is5xxServerError());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), hasSize(0));
        });
    }

    @Test
    public void givenNoCustomer_whenAddingItemToShoppingCart_thenNotFound() throws Exception {

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
    public void givenCustomerWithEmptyShoppingCart_whenAddingNonExistingItemToShoppingCart_thenShoppingCartIsNotUpdated() throws Exception {

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(UUID.randomUUID().toString(), 5);

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ShoppingCartErrorCode.PRODUCT_NOT_FOUND.getCode()));

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), hasSize(0));
        });
    }
}
