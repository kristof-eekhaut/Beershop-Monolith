package be.ordina.beershop.integrationTests.product;

import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.ProductTestData;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetProductsITest extends IntegrationTest {

    @Test
    public void whenNoProductsExist_thenEmptyPage() throws Exception {

        mockMvc.perform(
                get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(hasSize(0)));
    }

    @Test
    public void whenOneProductExists_thenPageWith1Item() throws Exception {

        persistProduct(ProductTestData.karmeliet().build());

        mockMvc.perform(
                get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Karmeliet Tripel"))
                .andExpect(jsonPath("$.content[0].price").value(1.20f))
                .andExpect(jsonPath("$.content[0].alcoholPercentage").value(7.5f))
                .andExpect(jsonPath("$.content[0].quantityIndicator").value("PLENTY_AVAILABLE"))
                .andExpect(jsonPath("$.content[0].weight.amount").value(100.00f))
                .andExpect(jsonPath("$.content[0].weight.unit").value("GRAM"));
    }

    @Test
    public void whenMultipleProductsExist_thenPageWithMultipleItems() throws Exception {

        persistProduct(ProductTestData.karmeliet().build());
        persistProduct(ProductTestData.westmalle().build());
        persistProduct(ProductTestData.westvleteren().build());

        mockMvc.perform(
                get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(hasSize(3)))
                .andExpect(jsonPath("$.content[*].name").value(containsInAnyOrder("Karmeliet Tripel", "Westmalle Tripel", "Westvleteren Blonde")));
    }
}
