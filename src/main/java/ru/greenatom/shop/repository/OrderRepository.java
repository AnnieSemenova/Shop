package ru.greenatom.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greenatom.shop.repository.entity.OrderEntity;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> getOrderEntitiesByUsername(String username);
}
