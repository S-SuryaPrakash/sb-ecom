package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.tool.schema.spi.SchemaTruncator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "Cart")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private Double totalPrice=0.0;
}
