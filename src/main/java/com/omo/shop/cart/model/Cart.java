package com.omo.shop.cart.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.omo.shop.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private Set<CartItem> items = new HashSet<>();
    @OneToOne(
            cascade = CascadeType.DETACH
    )
    @JoinColumn(name = "user_id")
    private User user;


    public void addItem(CartItem item) {
        this.items.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = items.stream().map(item -> {
            BigDecimal unitPrice = item.getUnitPrice();
            if (unitPrice == null) {
                return BigDecimal.ZERO;
            }
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
