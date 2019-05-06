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
import be.ordina.beershop.shoppingcart.ShoppingCartTestData;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RemoveItemFromShoppingCartITest extends IntegrationTest {

    @Test
    public void givenCustomerWithItemInShoppingCart_whenRemovingItem_thenShoppingCartIsEmpty() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        LineItem lineItem = LineItemTestData.lineItem(karmeliet).build();
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCartTestData.emptyCart()
                        .lineItem(lineItem)
                        .build())
                .build());

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/remove-product/" + karmeliet.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), hasSize(0));
        });
    }

    @Test
    public void givenCustomerWithMultipleItemsInShoppingCart_whenRemovingItem_thenItemIsRemovedAndOthersStayInShoppingCart() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());
        JPAProduct westmalle = persistProduct(JPAProductTestData.westmalle().build());

        LineItem lineItem = LineItemTestData.lineItem(karmeliet).build();
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCartTestData.emptyCart()
                        .lineItem(lineItem)
                        .lineItem(LineItemTestData.lineItem(westmalle).build())
                        .build())
                .build());

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/remove-product/" + karmeliet.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), hasSize(1));
            assertThat(shoppingCart.getLineItems().get(0),
                    LineItemMatcher.matchesLineItem(LineItemTestData.lineItem(westmalle).build())
            );
        });
    }

    @Test
    public void givenCustomerWithItemInShoppingCart_whenRemovingNonExistingItem_thenShoppingCartIsUnchanged() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        LineItem lineItem = LineItemTestData.lineItem(karmeliet).build();
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCartTestData.emptyCart()
                        .lineItem(lineItem)
                        .build())
                .build());

        mockMvc.perform(
                patch("/customers/" + customer.getId() + "/shopping-cart/remove-product/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), hasSize(1));
            assertThat(shoppingCart.getLineItems().get(0),
                    LineItemMatcher.matchesLineItem(lineItem)
            );
        });
    }

    @Test
    public void givenNoCustomer_whenRemovingItem_thenNotFound() throws Exception {

        mockMvc.perform(
                delete("/customers/" + UUID.randomUUID() + "/shopping-cart/line-items/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
