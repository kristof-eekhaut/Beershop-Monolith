package be.ordina.beershop.integrationTests;

import be.ordina.beershop.controller.OrderResource;
import be.ordina.beershop.domain.*;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.OrderRepository;
import be.ordina.beershop.repository.ProductRepository;
import be.ordina.beershop.shoppingcart.AddProductToShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    private UUID customerId;
    private UUID productId;
    private Address address;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                                 .build();
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus transactionStatus) {
                createCustomer();
            }
        });
    }

    @Test
    @Transactional
    public void test() throws Exception {
        final Customer customer = new Customer();
        customer.setId(customerId);

        final Weight weight = new Weight();
        weight.setAmount(new BigDecimal("100.00"));
        weight.setUnit(WeightUnit.GRAM);

        final ProductDto karmeliet = new ProductDto(
                "Karmeliet Tripel", 10, "1.20", "7.5", new WeightDto("100", "GRAM"));

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(karmeliet))
                        .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated());

        final Product product = new Product();
        product.setId(productRepository.findAll().get(0).getId());

        final AddProductToShoppingCart lineItem = new AddProductToShoppingCart(product.getId(), 3);

        mockMvc.perform(
                patch("/customers/" + customerId + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(lineItem))
                        .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());

        mockMvc.perform(
                post("/orders")
                        .content(objectMapper.writeValueAsString(new OrderResource(customerId)))
                        .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated());

        final List<Order> orders = orderRepository.findAll();

        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0)).satisfies(savedOrder -> {
            assertThat(savedOrder.getState()).isEqualByComparingTo(OrderStatus.CREATED);
            assertThat(savedOrder.getAddress()).isEqualTo(address);
            assertThat(savedOrder.getLineItems().size()).isEqualTo(1);
            assertThat(savedOrder.getLineItems().get(0)).satisfies(lineItem1 -> {
                assertThat(lineItem1.getQuantity()).isEqualTo(3);
                assertThat(lineItem1.getPrice()).isEqualTo(new BigDecimal("3.60"));
            });
        });
        mockMvc.perform(
                get("/products"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].name").value("Karmeliet Tripel"))
               .andExpect(jsonPath("$.content[0].price").value(1.20f))
               .andExpect(jsonPath("$.content[0].alcoholPercentage").value(7.5f))
               .andExpect(jsonPath("$.content[0].quantityIndicator").value("PLENTY_AVAILABLE"));
    }

    private void createCustomer() {
        address = new Address();
        address.setCountry("BE");
        address.setNumber("10");
        address.setStreet("Kerkstraat");
        address.setPostalCode("3000");

        final Customer customer = new Customer();
        customerId = UUID.randomUUID();
        customer.setId(customerId);
        customer.setName("Joske Vermeulen");
        customer.setAddress(address);
        customer.setBirthDate(LocalDate.of(1991, 10, 4));
        customerRepository.save(customer);
    }

}
