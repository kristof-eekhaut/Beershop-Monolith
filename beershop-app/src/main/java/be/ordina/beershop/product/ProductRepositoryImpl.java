package be.ordina.beershop.product;

import be.ordina.beershop.repository.AbstractJPARepository;
import be.ordina.beershop.repository.entities.JPADiscount;
import be.ordina.beershop.repository.entities.JPAProduct;
import be.ordina.beershop.repository.entities.JPAWeight;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static be.ordina.beershop.common.Price.price;
import static be.ordina.beershop.product.ProductId.productId;

@Component
public class ProductRepositoryImpl extends AbstractJPARepository<ProductId, Product, UUID, JPAProduct>
        implements ProductRepository {

    public ProductRepositoryImpl(JPAProductDAO jpaProductDAO,
                                 ApplicationEventPublisher eventPublisher) {
        super(jpaProductDAO, eventPublisher);
    }

    @Override
    public ProductId nextId() {
        return ProductId.productId(UUID.randomUUID());
    }

    @Override
    protected JPAProduct createEmptyJPAEntity(ProductId aggregateRootId) {
        return JPAProduct.builder().id(aggregateRootId.getValue()).build();
    }

    @Override
    protected void mapToJPAEntity(Product from, JPAProduct to) {
        to.setName(from.getName());
        to.setQuantity(from.getQuantity());
        to.setPrice(from.getPrice().getValue());
        to.setAlcoholPercentage(from.getAlcoholPercentage().orElse(null));
        to.setWeight(mapToJPAWeight(from.getWeight()));

        updateJPADiscounts(from, to);
    }

    @Override
    protected Product mapToDomain(JPAProduct jpaProduct) {
        return Product.builder()
                .id(productId(jpaProduct.getId()))
                .name(jpaProduct.getName())
                .quantity(jpaProduct.getQuantity())
                .price(price(jpaProduct.getPrice()))
                .discounts(jpaProduct.getDiscounts().stream()
                        .map(this::mapToDomain)
                        .collect(Collectors.toList()))
                .alcoholPercentage(jpaProduct.getAlcoholPercentage().orElse(null))
                .weight(mapToDomain(jpaProduct.getWeight()))
                .build();
    }

    @Override
    protected UUID mapToJPAId(ProductId aggregateRootId) {
        return aggregateRootId.getValue();
    }

    private Discount mapToDomain(JPADiscount jpaDiscount) {
        return Discount.builder()
                .percentage(jpaDiscount.getPercentage())
                .startDate(jpaDiscount.getStartDate())
                .endDate(jpaDiscount.getEndDate())
                .build();
    }

    private Weight mapToDomain(JPAWeight jpaWeight) {
        return Weight.weight(jpaWeight.getAmount(), jpaWeight.getUnit());
    }

    private JPAWeight mapToJPAWeight(Weight weight) {
        return JPAWeight.weight(weight.getAmount(), weight.getUnit());
    }

    private void updateJPADiscounts(Product from, JPAProduct to) {
        List<JPADiscount> existingDiscounts = to.getDiscounts();

        List<JPADiscount> updatedDiscounts = new ArrayList<>();
        for (Discount discount : from.getDiscounts()) {
            JPADiscount jpaDiscount = existingDiscounts.stream()
                    .filter(existingDiscount -> matchesDiscount(discount, existingDiscount))
                    .findAny()
                    .orElseGet(() -> JPADiscount.builder()
                            .id(UUID.randomUUID())
                            .percentage(discount.getPercentage())
                            .startDate(discount.getStartDate())
                            .endDate(discount.getEndDate())
                            .build());

            updatedDiscounts.add(jpaDiscount);
        }

        to.setDiscounts(updatedDiscounts);
    }

    private boolean matchesDiscount(Discount discount, JPADiscount jpaDiscount) {
        return discount.getPercentage().equals(jpaDiscount.getPercentage())
                && discount.getStartDate().equals(jpaDiscount.getStartDate())
                && discount.getEndDate().equals(jpaDiscount.getEndDate());
    }
}
