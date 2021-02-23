package ru.geekbrains.market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import ru.geekbrains.market.utils.Cart;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Column(name = "price")
    private Double price;

    /*delivery details*/

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private DeliveryAddress deliveryAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "create_at")
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @CreationTimestamp
    private LocalDateTime updateAt;

    @JsonIgnore
    @Transient
    private boolean confirmed;

    public Order(User user, Cart cart, String phoneNumber) {
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.orderItems = new ArrayList<>();
        for (OrderItem oi : cart.getItems()) {
            oi.setOrder(this);
            this.orderItems.add(oi);
        }
        this.price = cart.getTotalPrice();
//        cart.clear();
    }

}
