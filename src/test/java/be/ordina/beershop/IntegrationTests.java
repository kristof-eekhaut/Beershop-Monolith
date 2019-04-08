package be.ordina.beershop;

import be.ordina.beershop.domain.Customer;
import be.ordina.beershop.domain.LineItem;
import be.ordina.beershop.domain.Order;
import be.ordina.beershop.domain.Product;
import be.ordina.beershop.repository.CustomerRepository;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    private UUID customerId;
    private UUID productId;

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
    }

    private void createProduct() {
        productId = UUID.randomUUID();
        final Product product = new Product(productId, "Karmeliet", 10, new BigDecimal("1.00"));
        productRepository.save(product);
    }

    private void createCustomer() {
        final Customer customer = new Customer();
        customerId = UUID.randomUUID();
        customer.setId(customerId);
        customer.setName("Joske Vermeulen");
        customerRepository.save(customer);
    }

}
