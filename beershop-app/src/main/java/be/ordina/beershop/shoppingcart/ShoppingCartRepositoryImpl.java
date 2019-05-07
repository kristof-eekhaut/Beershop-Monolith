package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.common.Price;
import be.ordina.beershop.product.ProductId;
import be.ordina.beershop.repository.AbstractJPARepository;
import be.ordina.beershop.repository.entities.JPAShoppingCart;
import be.ordina.beershop.repository.entities.JPAShoppingCartItem;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static be.ordina.beershop.shoppingcart.CustomerId.customerId;
import static be.ordina.beershop.shoppingcart.ShoppingCartId.shoppingCartId;
import static java.util.Objects.requireNonNull;

@Component
public class ShoppingCartRepositoryImpl extends AbstractJPARepository<ShoppingCartId, ShoppingCart, UUID, JPAShoppingCart>
        implements ShoppingCartRepository {

    private JPAShoppingCartDAO jpaShoppingCartDAO;

    public ShoppingCartRepositoryImpl(JPAShoppingCartDAO jpaShoppingCartDAO,
                                      ApplicationEventPublisher eventPublisher) {
        super(jpaShoppingCartDAO, eventPublisher);
        this.jpaShoppingCartDAO = requireNonNull(jpaShoppingCartDAO);
    }

    @Override
    public ShoppingCartId nextId() {
        return shoppingCartId(UUID.randomUUID());
    }

    @Override
    public Optional<ShoppingCart> findByCustomerId(CustomerId customerId) {
        return jpaShoppingCartDAO.findByCustomerId(customerId.getValue())
                .map(this::mapToDomain);
    }

    @Override
    protected JPAShoppingCart createEmptyJPAEntity(ShoppingCartId aggregateRootId) {
        return JPAShoppingCart.builder().id(aggregateRootId.getValue()).build();
    }

    @Override
    protected ShoppingCart mapToDomain(JPAShoppingCart jpaShoppingCart) {
        return ShoppingCart.builder()
                .id(shoppingCartId(jpaShoppingCart.getId()))
                .customerId(customerId(jpaShoppingCart.getCustomerId()))
                .shoppingCartItems(jpaShoppingCart.getItems().stream()
                        .map(this::mapToDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    protected void mapToJPAEntity(ShoppingCart from, JPAShoppingCart to) {
        to.setCustomerId(from.getCustomerId().getValue());

        updateJPAShoppingCartItems(from, to);
    }

    @Override
    protected UUID mapToJPAId(ShoppingCartId aggregateRootId) {
        return aggregateRootId.getValue();
    }

    private ShoppingCartItem mapToDomain(JPAShoppingCartItem jpaShoppingCartItem) {
        return ShoppingCartItem.builder()
                .productId(ProductId.productId(jpaShoppingCartItem.getProductId()))
                .shoppingCartId(shoppingCartId(jpaShoppingCartItem.getShoppingCartId()))
                .quantity(jpaShoppingCartItem.getQuantity())
                .productPrice(Price.price(jpaShoppingCartItem.getProductPrice()))
                .build();
    }

    private void updateJPAShoppingCartItems(ShoppingCart from, JPAShoppingCart to) {
        List<JPAShoppingCartItem> existingItems = to.getItems();

        List<JPAShoppingCartItem> updatedItems = new ArrayList<>();
        for (ShoppingCartItem item : from.getItems()) {
            JPAShoppingCartItem jpaShoppingCartItem = existingItems.stream()
                    .filter(existingItem -> matchesShoppingCartItem(item, existingItem))
                    .findAny()
                    .orElseGet(() -> JPAShoppingCartItem.builder()
                            .id(createJPAShoppingCartItemId(item.getId()))
                            .build());

            mapToJPAEntity(item, jpaShoppingCartItem);
            updatedItems.add(jpaShoppingCartItem);
        }

        to.setItems(updatedItems);
    }

    private JPAShoppingCartItem.JPAShoppingCartItemId createJPAShoppingCartItemId(ShoppingCartItemId id) {
        return new JPAShoppingCartItem.JPAShoppingCartItemId(id.getShoppingCartId().getValue(), id.getProductId().getValue());
    }

    private void mapToJPAEntity(ShoppingCartItem item, JPAShoppingCartItem jpaShoppingCartItem) {
        jpaShoppingCartItem.setQuantity(item.getQuantity());
        jpaShoppingCartItem.setProductPrice(item.getProductPrice().getValue());
    }

    private boolean matchesShoppingCartItem(ShoppingCartItem item, JPAShoppingCartItem existingItem) {
        return item.getProductId().equals(ProductId.productId(existingItem.getProductId()));
    }
}
