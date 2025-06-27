package ru.greenatom.shop.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.greenatom.shop.domain.Order;
import ru.greenatom.shop.service.OrderService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(Principal principal) {
        return ResponseEntity.ok(orderService.createOrder(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getByUsername(Principal principal) {
        return ResponseEntity.ok(orderService.getByUsername(principal.getName()));
    }
}
