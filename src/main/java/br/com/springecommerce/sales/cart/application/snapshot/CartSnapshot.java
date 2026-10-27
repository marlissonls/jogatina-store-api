package br.com.springecommerce.sales.cart.application.snapshot;

import br.com.springecommerce.sales.cart.domain.model.Cart;

import java.util.List;

public record CartSnapshot(
        Cart cart,
        List<CartItemSnapshot> items
) {}
