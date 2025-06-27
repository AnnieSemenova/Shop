package ru.greenatom.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.greenatom.shop.domain.Cart;
import ru.greenatom.shop.domain.CartItem;
import ru.greenatom.shop.domain.Order;
import ru.greenatom.shop.repository.OrderItemRepository;
import ru.greenatom.shop.repository.OrderRepository;
import ru.greenatom.shop.repository.ProductRepository;
import ru.greenatom.shop.repository.entity.OrderEntity;
import ru.greenatom.shop.repository.entity.OrderItemEntity;
import ru.greenatom.shop.repository.entity.OrderStatus;
import ru.greenatom.shop.repository.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CartService cartService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(CartService cartService,
                        ProductRepository productRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // не самый лучший способ достать все записи, по-хорошему нужен запрос, который за раз достанет
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Order> getByUsername(String username) {
        List<OrderEntity> orderEntity = orderRepository.getOrderEntitiesByUsername(username);
        return orderEntity.stream().map(entity -> {
            List<OrderItemEntity> orderItemEntities = orderItemRepository.findOrderItemEntitiesByOrderId(
                    entity.getId()
            );
            return entity.toOrder(orderItemEntities);
        }).toList();

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Order getOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.getReferenceById(orderId);
        List<OrderItemEntity> orderItemEntities = orderItemRepository.findOrderItemEntitiesByOrderId(orderId);
        return orderEntity.toOrder(orderItemEntities);
    }

    // Шесть походов в базу, можно уменьшить, за счет написать более специализированных запросов
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Order createOrder(String username) {
        Cart cart = cartService.getCartByUsername(username);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUsername(cart.getUsername());
        orderEntity.setCreatedAt(LocalDateTime.now());
        // Переходы по статусам, то есть обработку order-ов не рассматриваем, но в относительно безопасном сценарии,
        // может быть отдельная job, которая будет созданные ордера посылать куда-то дальше, ака transactional outbox
        orderEntity.setStatus(OrderStatus.CREATED);

        Long orderId = orderRepository.save(orderEntity).getId();

        Set<Long> productIds = cart.getItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toSet());
        Map<Long, ProductEntity> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

        List<OrderItemEntity> orderItemEntities = cart.getItems().stream()
                .map(item -> {
                    OrderItemEntity orderItemEntity = new OrderItemEntity();
                    orderItemEntity.setProductId(item.getProductId());
                    orderItemEntity.setQuantity(item.getQuantity());
                    orderItemEntity.setPrice(productMap.get(item.getProductId()).getPrice());
                    orderItemEntity.setOrderId(orderId);
                    return orderItemEntity;
                })
                .toList();

        orderItemEntities = orderItemRepository.saveAll(orderItemEntities);
        cartService.clearCart(cart.getId()); // очистить корзину после заказа

        return orderEntity.toOrder(orderItemEntities);
    }
}
