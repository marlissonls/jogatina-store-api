package br.com.jogatinastore.sales.cart.application.snapshot;

import br.com.jogatinastore.sales.cart.domain.model.Cart;

import java.util.List;

public record CartSnapshot(
        Cart cart,
        List<CartItemSnapshot> items
) {}
