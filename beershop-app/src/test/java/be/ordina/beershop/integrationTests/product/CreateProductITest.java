package be.ordina.beershop.integrationTests.product;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.domain.WeightUnit;
import be.ordina.beershop.integrationTests.IntegrationTest;
import be.ordina.beershop.integrationTests.WeightDto;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static be.ordina.beershop.domain.Weight.weight;
import static be.ordina.beershop.product.ProductMatcher.matchesProduct;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateProductITest extends IntegrationTest {

    @Test
    public void whenCreatingNewProduct_thenProductIsPersisted() throws Exception {

        final CreateProductDTO createProduct = defaultCreateProductDTO().build();

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(createProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        runInTransaction(() -> {
            List<Product> persistedProducts = productRepository.findAll();
            assertThat(persistedProducts, hasSize(1));

            Product createdProduct = persistedProducts.get(0);
            assertThat(createdProduct, matchesProduct(Product.builder()
                    .name("Karmeliet Tripel")
                    .quantity(10)
                    .price(new BigDecimal("1.20"))
                    .alcoholPercentage(new BigDecimal("7.50"))
                    .weight(weight(new BigDecimal("100.00"), WeightUnit.GRAM))
                    .build()));
        });
    }

    @Test
    public void whenCreatingProductWithoutName_thenProductIsNotCreated() throws Exception {

        final CreateProductDTO createProduct = defaultCreateProductDTO().name(null).build();

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(createProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        runInTransaction(() -> {
            List<Product> persistedProducts = productRepository.findAll();
            assertThat(persistedProducts, hasSize(0));
        });
    }

    @Test
    public void whenCreatingProductWithoutPrice_thenProductIsNotCreated() throws Exception {

        final CreateProductDTO createProduct = defaultCreateProductDTO().price(null).build();

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(createProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        runInTransaction(() -> {
            List<Product> persistedProducts = productRepository.findAll();
            assertThat(persistedProducts, hasSize(0));
        });
    }

    @Test
    public void whenCreatingProductWithPrice0_thenProductIsNotCreated() throws Exception {

        final CreateProductDTO createProduct = defaultCreateProductDTO().price("0").build();

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(createProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        runInTransaction(() -> {
            List<Product> persistedProducts = productRepository.findAll();
            assertThat(persistedProducts, hasSize(0));
        });
    }

    @Test
    public void whenCreatingProductWithoutAlcoholPercentage_thenProductIsNotCreated() throws Exception {

        final CreateProductDTO createProduct = defaultCreateProductDTO().alcoholPercentage(null).build();

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(createProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        runInTransaction(() -> {
            List<Product> persistedProducts = productRepository.findAll();
            assertThat(persistedProducts, hasSize(0));
        });
    }

    @Test
    public void whenCreatingProductWithoutWeightAmount_thenProductIsNotCreated() throws Exception {

        final CreateProductDTO createProduct = defaultCreateProductDTO().weight(new WeightDto(null, "GRAM")).build();

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(createProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        runInTransaction(() -> {
            List<Product> persistedProducts = productRepository.findAll();
            assertThat(persistedProducts, hasSize(0));
        });
    }

    @Test
    public void whenCreatingProductWithoutWeightUnit_thenProductIsNotCreated() throws Exception {

        final CreateProductDTO createProduct = defaultCreateProductDTO().weight(new WeightDto("100", null)).build();

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(createProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        runInTransaction(() -> {
            List<Product> persistedProducts = productRepository.findAll();
            assertThat(persistedProducts, hasSize(0));
        });
    }

    private CreateProductDTO.Builder defaultCreateProductDTO() {
        return CreateProductDTO.builder()
                .name("Karmeliet Tripel")
                .quantity(10)
                .price("1.20")
                .alcoholPercentage("7.5")
                .weight(new WeightDto("100", "GRAM"));
    }
}
