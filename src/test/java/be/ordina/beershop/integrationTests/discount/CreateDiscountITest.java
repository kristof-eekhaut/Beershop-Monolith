package be.ordina.beershop.integrationTests.discount;

import be.ordina.beershop.domain.Discount;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.ProductTestData;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

import static be.ordina.beershop.discount.DiscountMatcher.matchesDiscount;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateDiscountITest extends IntegrationTest {

    @Test
    @Ignore // TODO: this functionality doesn't work...
    public void givenCustomerWithShoppingCart_whenCreatingNewOrder_thenOrderIsCreatedAndShoppingCartIsCleared() throws Exception {

        Product karmeliet = persistProduct(ProductTestData.karmeliet().build());

        final CreateDiscountDTO createDiscountDTO = CreateDiscountDTO.builder()
                .productId(karmeliet.getId())
                .percentage(new BigDecimal("15"))
                .startDate(LocalDate.of(2018, 1, 1).atStartOfDay(ZoneId.systemDefault()))
                .endDate(LocalDate.of(2018, 12, 31).atStartOfDay(ZoneId.systemDefault()))
                .build();

        mockMvc.perform(
                post("/discounts")
                        .content(objectMapper.writeValueAsString(createDiscountDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            Product updatedProduct = productRepository.findById(karmeliet.getId()).get();
            assertThat(updatedProduct.getDiscounts(), containsInAnyOrder(
                    matchesDiscount(Discount.builder()
                            .productId(karmeliet.getId())
                            .percentage(new BigDecimal("15"))
                            .startDate(LocalDate.of(2018, 1, 1).atStartOfDay(ZoneId.systemDefault()))
                            .endDate(LocalDate.of(2018, 12, 31).atStartOfDay(ZoneId.systemDefault()))
                            .build())
            ));
        });
    }
}
