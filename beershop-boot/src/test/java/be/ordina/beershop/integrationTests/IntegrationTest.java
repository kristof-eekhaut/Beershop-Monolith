package be.ordina.beershop.integrationTests;

import be.ordina.beershop.repository.entities.Customer;
import be.ordina.beershop.repository.entities.Order;
import be.ordina.beershop.repository.*;
import be.ordina.beershop.repository.entities.JPAProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(value = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public abstract class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JPAProductDAO jpaProductDAO;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected CustomerRepository customerRepository;
    @Autowired
    protected ShoppingCartRepository shoppingCartRepository;

    protected JPAProduct persistProduct(JPAProduct product) {
        return runInTransaction(() -> jpaProductDAO.save(product));
    }

    protected Order persistOrder(Order order) {
        return runInTransaction(() -> orderRepository.save(order));
    }

    protected Customer persistCustomer(Customer customer) {
        return runInTransaction(() -> customerRepository.save(customer));
    }

    protected void runInTransaction(RunnableWithException runnable) {
        transactionTemplate.execute(status -> {
            try {
                runnable.run();
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new IllegalStateException(e);
            }
            return null;
        });
    }

    protected <T> T runInTransaction(SupplierWithException<T> supplier) {
        return transactionTemplate.execute(status -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new IllegalStateException(e);
            }
        });
    }

    @FunctionalInterface
    public interface RunnableWithException {
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface SupplierWithException<R> {
        R get() throws Exception;
    }
}
