package be.ordina.beershop;

import be.ordina.beershop.domain.Address;
import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.OrderStatus;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.repository.CustomerRepository;
import be.ordina.beershop.repository.OrderRepository;
import be.ordina.beershop.repository.ProductRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                createProduct();
            }
        });
    }

    @Test
    @Transactional
    public void test() throws Exception {
        final Customer customer = new Customer();
        customer.setId(customerId);

        final Product karmeliet = new Product();
        karmeliet.setId(productId);

        final LineItem lineItem = new LineItem();
        lineItem.setProduct(karmeliet);
        lineItem.setQuantity(3);

        final Order order = new Order();
        order.setCustomer(customer);
        order.setLineItems(Collections.singletonList(lineItem));

        mockMvc.perform(
                post("/orders")
                        .content(objectMapper.writeValueAsString(order))
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
                assertThat(lineItem1.getPrice()).isEqualTo(new BigDecimal("3.00"));
            });
        });

    }

    @Test
    public void testCatalogue() throws Exception {
        mockMvc.perform(
                get("/products"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].name").value("Karmeliet"))
               .andExpect(jsonPath("$.content[0].price").value(1.00f))
               .andExpect(jsonPath("$.content[0].alcoholPercentage").value(14.2f))
               .andExpect(jsonPath("$.content[0].quantityIndicator").value("SOLD_OUT"));
    }

    private void createProduct() {
        productId = UUID.randomUUID();
        final Product product = new Product(productId, "Karmeliet", 0, new BigDecimal("1.00"), new BigDecimal("14.2"));
        productRepository.save(product);
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
        customerRepository.save(customer);
    }

}
