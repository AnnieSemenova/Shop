package ru.greenatom.shop.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.greenatom.shop.domain.Cart;
import ru.greenatom.shop.service.CartService;

import java.security.Principal;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{productId}")
    public ResponseEntity<Cart> addToCart(
            Principal principal,
            @PathVariable Long productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(principal.getName(), productId, quantity));
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(Principal principal) {
        return ResponseEntity.ok(cartService.getOrCreateCart(principal.getName()));
    }

}