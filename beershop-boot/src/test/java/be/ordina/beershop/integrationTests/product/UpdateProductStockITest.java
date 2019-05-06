package be.ordina.beershop.integrationTests.product;

import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.product.WeightUnit;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.exception.ProductErrorCode;
import be.ordina.beershop.product.JPAProductMatcher;
import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.product.UpdateProductStock;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static be.ordina.beershop.repository.entities.JPAWeight.weight;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateProductStockITest extends IntegrationTest {

    @Test
    public void givenProduct_whenUpdatingProductQuantity_thenProductIsUpdated() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        final UpdateProductStock updateProduct = new UpdateProductStock(15);

        mockMvc.perform(
                patch("/products/" + karmeliet.getId() + "/update-stock")
                        .content(objectMapper.writeValueAsString(updateProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        runInTransaction(() -> {
            JPAProduct updatedProduct = jpaProductDAO.findById(karmeliet.getId()).get();
            MatcherAssert.assertThat(updatedProduct, JPAProductMatcher.matchesProduct(JPAProduct.builder()
                    .name("Karmeliet Tripel")
                    .quantity(15)
                    .price(new BigDecimal("1.20"))
                    .alcoholPercentage(new BigDecimal("7.50"))
                    .weight(weight(new BigDecimal("100.00"), WeightUnit.GRAM))
                    .build()));
        });
    }

    @Test
    public void givenProduct_whenUpdatingProductQuantityToLessThan0_thenProductIsNotUpdated() throws Exception {

        JPAProduct karmeliet = persistProduct(JPAProductTestData.karmeliet().build());

        final UpdateProductStock updateProduct = new UpdateProductStock(-1);

        mockMvc.perform(
                patch("/products/" + karmeliet.getId() + "/update-stock")
                        .content(objectMapper.writeValueAsString(updateProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ProductErrorCode.INVALID_PRODUCT_QUANTITY.getCode()));

        runInTransaction(() -> {
            JPAProduct updatedProduct = jpaProductDAO.findById(karmeliet.getId()).get();
            MatcherAssert.assertThat(updatedProduct, JPAProductMatcher.matchesProduct(JPAProduct.builder()
                    .name("Karmeliet Tripel")
                    .quantity(10)
                    .price(new BigDecimal("1.20"))
                    .alcoholPercentage(new BigDecimal("7.50"))
                    .weight(weight(new BigDecimal("100.00"), WeightUnit.GRAM))
                    .build()));
        });
    }
}
