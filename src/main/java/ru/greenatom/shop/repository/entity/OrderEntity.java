package ru.greenatom.shop.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.greenatom.shop.domain.Order;
import ru.greenatom.shop.domain.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "shop_order")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order toOrder(List<OrderItemEntity> orderItemEntityList) {
        return Order.builder()
                .id(this.id)
                .createdAt(this.createdAt)
                .status(this.status)
                .username(this.username)
                .items(orderItemEntityList.stream().map(entity ->
                                OrderItem.builder()
                                        .id(entity.getId())
                                        .price(entity.getPrice())
                                        .productId(entity.getProductId())
                                        .quantity(entity.getQuantity())
                                        .build()
                        ).toList()
                ).build();
    }
}
