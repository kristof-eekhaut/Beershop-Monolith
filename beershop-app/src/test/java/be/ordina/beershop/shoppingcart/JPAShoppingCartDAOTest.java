package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.product.JPAProductTestData;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.JPAShoppingCart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static be.ordina.beershop.shoppingcart.JPAShoppingCartTestData.cartWithItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@ExtendWith(SpringExtension.class)
class JPAShoppingCartDAOTest {

    @Autowired
    private JPAShoppingCartDAO jpaShoppingCartDAO;

    @Test
    void findByCustomerId_whenShoppingCartIsFound_thenItIsReturned() {
        UUID customerId = UUID.randomUUID();
        JPAProduct product = JPAProductTestData.karmeliet().build();
        JPAShoppingCart shoppingCart = jpaShoppingCartDAO.save(cartWithItems(customerId, product).build());

        Optional<JPAShoppingCart> foundShoppingCart = jpaShoppingCartDAO.findByCustomerId(customerId);

        assertTrue(foundShoppingCart.isPresent());
        assertThat(foundShoppingCart.get().getId(), equalTo(shoppingCart.getId()));

    }

    @Test
    void findByCustomerId_whenShoppingCartIsNotFound_thenEmptyIsReturned() {
        UUID customerId = UUID.randomUUID();

        Optional<JPAShoppingCart> foundShoppingCart = jpaShoppingCartDAO.findByCustomerId(customerId);

        assertFalse(foundShoppingCart.isPresent());
    }
}