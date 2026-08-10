package br.com.jogatinastore.domain.sales.cart.snapshot;

import br.com.jogatinastore.domain.sales.cart.entity.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartSnapshot(
        Cart cart,
        List<CartItemSnapshot> items
) {}
