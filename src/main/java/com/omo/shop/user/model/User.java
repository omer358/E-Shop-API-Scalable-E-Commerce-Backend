package com.omo.shop.user.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.omo.shop.address.model.Address;
import com.omo.shop.cart.model.Cart;
import com.omo.shop.order.model.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId
    private String email;
    private String password;
    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Cart cart;
    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.MERGE, CascadeType.DETACH}
    )
    @JsonManagedReference
    private List<Order> orders;
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH}
    )
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id")
    )
    private Collection<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();

}
