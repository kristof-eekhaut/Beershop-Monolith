package be.ordina.beershop.integrationTests;

import be.ordina.beershop.BeershopApplication;
import be.ordina.beershop.order.CreateOrder;
import be.ordina.beershop.product.CreateProduct;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.JPAProductDAO;
import be.ordina.beershop.repository.OrderRepository;
import be.ordina.beershop.repository.entities.Address;
import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.Order;
import be.ordina.beershop.repository.entities.OrderStatus;
import be.ordina.beershop.shoppingcart.AddProductToShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BeershopApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class IntegrationTests {

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
    private JPAProductDAO jpaProductDAO;
    @Autowired
    private OrderRepository orderRepository;

    private UUID customerId;
    private Address address;

    @BeforeEach
    void setup() {
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
    void test() throws Exception {
        final Customer customer = new Customer();
        customer.setId(customerId);

        final CreateProduct createProduct = CreateProduct.builder()
                .name("Karmeliet Tripel")
                .quantity(10)
                .price(new BigDecimal("1.20"))
                .alcoholPercentage(new BigDecimal("7.5"))
                .weight(new BigDecimal("100"), "GRAM")
                .build();

        mockMvc.perform(
                post("/products")
                        .content(objectMapper.writeValueAsString(createProduct))
                        .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated());

        UUID productId = jpaProductDAO.findAll().get(0).getId();

        final AddProductToShoppingCart addProductToShoppingCart = new AddProductToShoppingCart(productId.toString(), 3);

        mockMvc.perform(
                patch("/customers/" + customerId + "/shopping-cart/add-product")
                        .content(objectMapper.writeValueAsString(addProductToShoppingCart))
                        .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk());

        mockMvc.perform(
                post("/orders")
                        .content(objectMapper.writeValueAsString(new CreateOrder(customerId.toString())))
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
//                assertThat(lineItem1.getPrice()).isEqualTo(new BigDecimal("3.60"));
            });
        });
        mockMvc.perform(
                get("/products"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].name").value("Karmeliet Tripel"))
               .andExpect(jsonPath("$.content[0].price").value(1.20f))
               .andExpect(jsonPath("$.content[0].alcoholPercentage").value(7.5f))
               .andExpect(jsonPath("$.content[0].quantity").value(10));
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
