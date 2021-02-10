package ru.geekbrains.market.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "item_price")
    private Double itemPrice;

    @Column(name = "total_price")
    private Double totalPrice;

    public OrderItem(Product p) {
        this.product = p;
        this.quantity = 1L;
        this.totalPrice = p.getPrice();
        this.itemPrice = p.getPrice();
    }

    public void incrementQuantity() {
        quantity++;
        totalPrice = itemPrice * quantity;
    }

    public void decrementQuantity() {
        quantity--;
        totalPrice = itemPrice * quantity;
    }
}
