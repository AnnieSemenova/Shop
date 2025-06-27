package ru.greenatom.shop.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItem {

    private Long id;
    private Long productId;
    private Integer quantity;
}
