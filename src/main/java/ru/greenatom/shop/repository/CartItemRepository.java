package ru.greenatom.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.greenatom.shop.repository.entity.CartItemEntity;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    List<CartItemEntity> findCartItemEntitiesByCartId(Long cartId);

    void deleteAllByCartId(Long cartId);
}
