package ru.greenatom.shop.domain;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Cart {

    private Long id;

    private String username;

    private List<CartItem> items;
}
