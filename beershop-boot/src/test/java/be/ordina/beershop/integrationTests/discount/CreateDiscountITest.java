package be.ordina.beershop.integrationTests.discount;

import be.ordina.beershop.discount.CreateDiscount;
import be.ordina.beershop.discount.JPADiscountMatcher;
import be.ordina.beershop.repository.entities.JPADiscount;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.JPAProductTestData;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateDiscountITest extends IntegrationTest {

    @Test
    public void givenProduct_whenCreatingDiscount_thenDiscountIsAddedToProduct() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        final CreateDiscount createDiscount = CreateDiscount.builder()
                .productId(karmeliet.getId().toString())
                .percentage(new BigDecimal("15"))
                .startDate(LocalDate.of(2018, 1, 1))
                .endDate(LocalDate.of(2018, 12, 31))
                .build();

        mockMvc.perform(
                post("/discounts")
                        .content(objectMapper.writeValueAsString(createDiscount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            JPAProduct updatedProduct = jpaProductDAO.findById(karmeliet.getId()).get();

            assertThat(updatedProduct.getDiscounts(), hasSize(1));
            MatcherAssert.assertThat(updatedProduct.getDiscounts().get(0),
                    JPADiscountMatcher.matchesDiscount(JPADiscount.builder()
                            .percentage(new BigDecimal("15.00"))
                            .startDate(LocalDate.of(2018, 1, 1))
                            .endDate(LocalDate.of(2018, 12, 31))
                            .build())
            );
        });
    }

    @Test
    public void givenNoProduct_whenCreatingDiscount_thenNotFound() throws Exception {

        final CreateDiscount createDiscount = CreateDiscount.builder()
                .productId(UUID.randomUUID().toString())
                .percentage(new BigDecimal("15"))
                .startDate(LocalDate.of(2018, 1, 1))
                .endDate(LocalDate.of(2018, 12, 31))
                .build();

        mockMvc.perform(
                post("/discounts")
                        .content(objectMapper.writeValueAsString(createDiscount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
