package ru.greenatom.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greenatom.shop.repository.entity.OrderItemEntity;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findOrderItemEntitiesByOrderId(Long cartId);
}
