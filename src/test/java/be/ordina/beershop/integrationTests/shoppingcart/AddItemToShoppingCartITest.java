package be.ordina.beershop.integrationTests.shoppingcart;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.domain.ShoppingCart;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.LineItemTestData;
import be.ordina.beershop.product.ProductTestData;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.UUID;

import static be.ordina.beershop.order.LineItemMatcher.matchesLineItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddItemToShoppingCartITest extends IntegrationTest {

    @Test
    public void givenCustomerWithEmptyShoppingCart_whenAddingItemToShoppingCart_thenShoppingCartContainsItem() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddItemToShoppingCartDTO addItemToShoppingCartDTO = new AddItemToShoppingCartDTO(karmeliet.getId(), 5);

        mockMvc.perform(
                post("/customers/" + customer.getId() + "/shopping-cart/line-items")
                        .content(objectMapper.writeValueAsString(addItemToShoppingCartDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), containsInAnyOrder(
                    matchesLineItem(LineItem.builder()
                            .product(karmeliet)
                            .quantity(5)
                            .price(new BigDecimal("6.00"))
                            .build())
            ));
        });
    }

    @Test
    public void givenCustomerWithItemInShoppingCart_whenAddingOtherItemToShoppingCart_thenShoppingCartContains2Items() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());
        Product westmalle = persistProduct(ProductTestData.westmalle().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCart.builder()
                        .id(UUID.randomUUID())
                        .lineItem(LineItemTestData.lineItem(karmeliet).build())
                        .build())
                .build());

        final AddItemToShoppingCartDTO addItemToShoppingCartDTO = new AddItemToShoppingCartDTO(westmalle.getId(), 4);

        mockMvc.perform(
                post("/customers/" + customer.getId() + "/shopping-cart/line-items")
                        .content(objectMapper.writeValueAsString(addItemToShoppingCartDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), containsInAnyOrder(
                    matchesLineItem(LineItemTestData.lineItem(karmeliet).build()),
                    matchesLineItem(LineItem.builder()
                            .product(westmalle)
                            .quantity(4)
                            .price(new BigDecimal("5.20"))
                            .build())
            ));
        });
    }

    @Test
    public void givenCustomerWithItemInShoppingCart_whenAddingMoreOfTheSameItemToShoppingCart_thenShoppingCartQuantityIsIncreased() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCart.builder()
                        .id(UUID.randomUUID())
                        .lineItem(LineItemTestData.lineItem(karmeliet).build())
                        .build())
                .build());

        final AddItemToShoppingCartDTO addItemToShoppingCartDTO = new AddItemToShoppingCartDTO(karmeliet.getId(), 4);

        mockMvc.perform(
                post("/customers/" + customer.getId() + "/shopping-cart/line-items")
                        .content(objectMapper.writeValueAsString(addItemToShoppingCartDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            // TODO: these should be added together into 1 LineItem
            assertThat(shoppingCart.getLineItems(), containsInAnyOrder(
                    matchesLineItem(LineItemTestData.lineItem(karmeliet).build()),
                    matchesLineItem(LineItem.builder()
                            .product(karmeliet)
                            .quantity(4)
                            .price(new BigDecimal("4.80"))
                            .build())
            ));
        });
    }

    @Test
    public void givenCustomerWithEmptyShoppingCart_whenAddingItemWithQuantity0ToShoppingCart_thenShoppingCartIsNotUpdated() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());
        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddItemToShoppingCartDTO addItemToShoppingCartDTO = new AddItemToShoppingCartDTO(karmeliet.getId(), 0);

        mockMvc.perform(
                post("/customers/" + customer.getId() + "/shopping-cart/line-items")
                        .content(objectMapper.writeValueAsString(addItemToShoppingCartDTO))
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

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        final AddItemToShoppingCartDTO addItemToShoppingCartDTO = new AddItemToShoppingCartDTO(karmeliet.getId(), 5);

        mockMvc.perform(
                post("/customers/" + UUID.randomUUID() + "/shopping-cart/line-items")
                        .content(objectMapper.writeValueAsString(addItemToShoppingCartDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenCustomerWithEmptyShoppingCart_whenAddingNonExistingItemToShoppingCart_thenShoppingCartIsNotUpdated() throws Exception {

        Customer customer = persistCustomer(CustomerTestData.manVanMelle().build());

        final AddItemToShoppingCartDTO addItemToShoppingCartDTO = new AddItemToShoppingCartDTO(UUID.randomUUID(), 5);

        mockMvc.perform(
                post("/customers/" + customer.getId() + "/shopping-cart/line-items")
                        .content(objectMapper.writeValueAsString(addItemToShoppingCartDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), hasSize(0));
        });
    }
}
