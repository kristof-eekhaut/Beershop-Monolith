package be.ordina.beershop.integrationTests.shoppingcart;

import be.ordina.beershop.customer.CustomerTestData;
import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.domain.ShoppingCart;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.order.LineItemTestData;
import be.ordina.beershop.product.ProductTestData;
import be.ordina.beershop.shoppingcart.ShoppingCartTestData;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static be.ordina.beershop.order.LineItemMatcher.matchesLineItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RemoveItemFromShoppingCartITest extends IntegrationTest {

    @Test
    public void givenCustomerWithItemInShoppingCart_whenRemovingItem_thenShoppingCartIsEmpty() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        LineItem lineItem = LineItemTestData.lineItem(karmeliet).build();
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCartTestData.emptyCart()
                        .lineItem(lineItem)
                        .build())
                .build());

        mockMvc.perform(
                delete("/customers/" + customer.getId() + "/shopping-cart/line-items/" + karmeliet.getId())
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

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());
        Product westmalle = persistProduct(ProductTestData.westmalle().build());

        LineItem lineItem = LineItemTestData.lineItem(karmeliet).build();
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCartTestData.emptyCart()
                        .lineItem(lineItem)
                        .lineItem(LineItemTestData.lineItem(westmalle).build())
                        .build())
                .build());

        mockMvc.perform(
                delete("/customers/" + customer.getId() + "/shopping-cart/line-items/" + karmeliet.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), containsInAnyOrder(
                    matchesLineItem(LineItemTestData.lineItem(westmalle).build())
            ));
        });
    }

    @Test
    public void givenCustomerWithItemInShoppingCart_whenRemovingNonExistingItem_thenShoppingCartIsUnchanged() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        LineItem lineItem = LineItemTestData.lineItem(karmeliet).build();
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCartTestData.emptyCart()
                        .lineItem(lineItem)
                        .build())
                .build());

        mockMvc.perform(
                delete("/customers/" + customer.getId() + "/shopping-cart/line-items/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Customer updateCustomer = customerRepository.findById(customer.getId()).get();
            ShoppingCart shoppingCart = updateCustomer.getShoppingCart();

            assertThat(shoppingCart.getLineItems(), containsInAnyOrder(
                    matchesLineItem(lineItem)
            ));
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
