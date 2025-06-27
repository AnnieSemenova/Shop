package ru.greenatom.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.greenatom.shop.domain.Cart;
import ru.greenatom.shop.domain.CartItem;
import ru.greenatom.shop.repository.CartItemRepository;
import ru.greenatom.shop.repository.CartRepository;
import ru.greenatom.shop.repository.entity.CartEntity;
import ru.greenatom.shop.repository.entity.CartItemEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


// В идеале нужен еще слой между сервисами и репозиториям, по конвертации доменной сущности в entity и назад, и
// сбору доменной сущности из набора entity, опускаем для упрощения
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Cart getOrCreateCart(String username) {
        return get(username).orElseGet(() -> {
            CartEntity cart = new CartEntity();
            cart.setUsername(username);
            cart = cartRepository.save(cart);
            return Cart.builder()
                    .id(cart.getId())
                    .username(username)
                    .items(Collections.emptyList())
                    .build();
        });
    }

    private Optional<Cart> get(String username) {
        Optional<CartEntity> cartEntity = cartRepository.findByUsername(username);
        if (cartEntity.isPresent()) {
            CartEntity entity = cartEntity.get();
            // упрощаем, можно написать отдельный запрос, который получает сразу все данные и потом конвертирует их в объект
            List<CartItemEntity> entityItems = cartItemRepository.findCartItemEntitiesByCartId(entity.getId());
            return Optional.of(entity.toCart(entityItems));
        } else {
            return Optional.empty();
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Cart getCartByUsername(String username) {
        Optional<CartEntity> cartEntity = cartRepository.findByUsername(username);
        if (cartEntity.isPresent()) {
            CartEntity entity = cartEntity.get();
            List<CartItemEntity> entityItems = cartItemRepository.findCartItemEntitiesByCartId(entity.getId());
            return entity.toCart(entityItems);
        }
        throw new RuntimeException("Object not found");
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Cart addToCart(String userId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        // Если заказов много, можно доставать сразу в map, чтобы не делать каждый раз поиск
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            CartItemEntity entity = CartItemEntity.builder()
                    .id(item.getId())
                    .cartId(cart.getId())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity() + quantity)
                    .build();
            cartItemRepository.save(entity);
        } else {
            CartItemEntity entity = CartItemEntity.builder()
                    .cartId(cart.getId())
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(entity);
        }
        return get(userId).orElseThrow();
    }

    public void clearCart(Long cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
        cartRepository.deleteById(cartId);
    }
}
