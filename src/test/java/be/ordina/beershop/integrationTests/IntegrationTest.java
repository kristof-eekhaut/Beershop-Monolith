package be.ordina.beershop.integrationTests;

import be.ordina.beershop.domain.Product;
import be.ordina.beershop.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ProductRepository productRepository;

    @Before
    public void setUp() {
        clearDataBase();
    }

    @After
    public void tearDown() {
        clearDataBase();
    }

    protected Product persistProduct(Product product) {
        return runInTransaction(() -> productRepository.save(product));
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

    private void clearDataBase() {
        productRepository.deleteAll();
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
