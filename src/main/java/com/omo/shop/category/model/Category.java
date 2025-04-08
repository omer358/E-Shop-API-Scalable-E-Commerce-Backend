package com.omo.shop.category.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omo.shop.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;
}
