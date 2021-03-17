package ru.geekbrains.market.entities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.market.entities.OrderItem;

@Data
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private Double price;
    private Long quantity;
    private String productTitle;

    public OrderItemDto(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.price = orderItem.getTotalPrice();
        this.quantity = orderItem.getQuantity();
        this.productTitle = orderItem.getProduct().getTitle();
    }
}
