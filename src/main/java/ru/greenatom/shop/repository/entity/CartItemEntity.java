package ru.greenatom.shop.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_item")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cartId;
    // Только id для простоты, по хорошему в корзине нужно имя объекта, его можно подтягивать в UI на лету по id,
    // либо энричить на бекенде в момент формирования ответа
    private Long productId;
    private Integer quantity;

}
