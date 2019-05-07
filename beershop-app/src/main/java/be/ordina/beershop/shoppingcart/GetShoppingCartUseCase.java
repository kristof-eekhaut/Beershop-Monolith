package be.ordina.beershop.shoppingcart;

import be.ordina.beershop.shoppingcart.dto.ShoppingCartItemDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
public class GetShoppingCartUseCase {

    private final ShoppingCartRepository shoppingCartRepository;

    GetShoppingCartUseCase(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = requireNonNull(shoppingCartRepository);
    }

    Optional<ShoppingCartView> execute(String customerId) {
        return shoppingCartRepository.findByCustomerId(CustomerId.fromString(customerId))
                .map(this::toShoppingCartView);
    }

    private ShoppingCartView toShoppingCartView(ShoppingCart shoppingCart) {
        return ShoppingCartView.builder()
                .id(shoppingCart.getId().toString())
                .customerId(shoppingCart.getCustomerId().toString())
                .items(shoppingCart.getItems().stream()
                        .map(this::toShoppingCartItemDTO)
                        .collect(Collectors.toList()))
                .totalPrice(shoppingCart.calculateTotalPrice().getValue())
                .build();
    }

    private ShoppingCartItemDTO toShoppingCartItemDTO(ShoppingCartItem shoppingCartItem) {
        return ShoppingCartItemDTO.builder()
                .productId(shoppingCartItem.getProductId().toString())
                .quantity(shoppingCartItem.getQuantity())
                .productPrice(shoppingCartItem.getProductPrice().getValue())
                .totalPrice(shoppingCartItem.getTotalPrice().getValue())
                .build();
    }
}
