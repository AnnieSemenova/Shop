package ru.greenatom.shop.domain;

import ru.greenatom.shop.dto.ProductDTO;

public class Product {

    private Long id;
    private String name;
    private Double price;

    public Product(ProductDTO dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
    }
}
