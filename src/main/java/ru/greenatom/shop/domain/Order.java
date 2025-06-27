package ru.greenatom.shop.domain;

import lombok.Builder;
import lombok.Data;
import ru.greenatom.shop.repository.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Order {

    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<OrderItem> items;
}
