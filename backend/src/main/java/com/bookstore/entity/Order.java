package com.bookstore.entity;

import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    public static Order fromCart(CartResponse cart) {

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .totalAmount(cart.total())
                .status(OrderStatus.CONFIRMED)
                .build();

        for (CartItemResponse item : cart.items()) {

            OrderItem orderItem = OrderItem.builder()
                    .bookId(item.bookId())
                    .title(item.title())
                    .price(item.price())
                    .quantity(item.quantity())
                    .itemTotal(item.itemTotal())
                    .build();

            order.addItem(orderItem);
        }

        return order;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}