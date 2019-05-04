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
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.UUID;

import static be.ordina.beershop.order.LineItemMatcher.matchesLineItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChangeQuantityOfItemInShoppingCartITest extends IntegrationTest {

    @Test
    @Ignore // TODO: this functionality doesn't work...
    public void givenCustomerWithItemInShoppingCart_whenChangingQuantity_thenItemQuantityIsUpdated() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        LineItem lineItem = LineItemTestData.lineItem(karmeliet).build();
        Customer customer = persistCustomer(CustomerTestData.manVanMelle()
                .shoppingCart(ShoppingCartTestData.emptyCart()
                        .lineItem(lineItem)
                        .build())
                .build());

        final ChangeQuantityOfItemInShoppingCartDTO changeQuantityOfItemInShoppingCartDTO = new ChangeQuantityOfItemInShoppingCartDTO(karmeliet.getId(), 5);

        mockMvc.perform(
                put("/customers/" + customer.getId() + "/shopping-cart/line-items/" + lineItem.getId())
                        .content(objectMapper.writeValueAsString(changeQuantityOfItemInShoppingCartDTO))
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
    public void givenNoCustomer_whenChangingQuantity_thenNotFound() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        final ChangeQuantityOfItemInShoppingCartDTO changeQuantityOfItemInShoppingCartDTO = new ChangeQuantityOfItemInShoppingCartDTO(karmeliet.getId(), 5);

        mockMvc.perform(
                put("/customers/" + UUID.randomUUID() + "/shopping-cart/line-items/" + UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(changeQuantityOfItemInShoppingCartDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}