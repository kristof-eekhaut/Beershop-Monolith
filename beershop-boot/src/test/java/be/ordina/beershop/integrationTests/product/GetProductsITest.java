package be.ordina.beershop.integrationTests.product;

import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.product.JPAProductTestData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetProductsITest extends IntegrationTest {

    @Test
    void whenNoProductsExist_thenEmptyPage() throws Exception {

        mockMvc.perform(
                get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(hasSize(0)));
    }

    @Test
    void whenOneProductExists_thenPageWith1Item() throws Exception {

        persistProduct(JPAProductTestData.karmeliet().build());

        mockMvc.perform(
                get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Karmeliet Tripel"))
                .andExpect(jsonPath("$.content[0].price").value(1.20f))
                .andExpect(jsonPath("$.content[0].alcoholPercentage").value(7.5f))
                .andExpect(jsonPath("$.content[0].quantity").value(10))
                .andExpect(jsonPath("$.content[0].weight.amount").value(100.00f))
                .andExpect(jsonPath("$.content[0].weight.unit").value("GRAM"));
    }

    @Test
    void whenMultipleProductsExist_thenPageWithMultipleItems() throws Exception {

        persistProduct(JPAProductTestData.karmeliet().build());
        persistProduct(JPAProductTestData.westmalle().build());
        persistProduct(JPAProductTestData.westvleteren().build());

        mockMvc.perform(
                get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(hasSize(3)))
                .andExpect(jsonPath("$.content[*].name").value(containsInAnyOrder("Karmeliet Tripel", "Westmalle Tripel", "Westvleteren Blonde")));
    }
}
