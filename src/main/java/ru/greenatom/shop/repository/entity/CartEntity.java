package ru.greenatom.shop.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.greenatom.shop.domain.Cart;
import ru.greenatom.shop.domain.CartItem;

import java.util.List;

@Data
@Entity
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    public Cart toCart(List<CartItemEntity> cartItemEntities) {
        return Cart.builder()
                .id(this.id)
                .username(this.username)
                .items(cartItemEntities.stream().map(cartItemEntity ->
                                CartItem.builder()
                                        .id(cartItemEntity.getId())
                                        .productId(cartItemEntity.getProductId())
                                        .quantity(cartItemEntity.getQuantity())
                                        .build()
                        ).toList()
                ).build();
    }
}
