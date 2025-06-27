package ru.greenatom.shop.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.greenatom.shop.dto.ProductDTO;

@Data
@Entity
@NoArgsConstructor
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;

    public ProductEntity(ProductDTO dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
    }
}
