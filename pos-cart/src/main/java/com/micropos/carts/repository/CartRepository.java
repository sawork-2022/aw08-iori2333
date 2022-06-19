package com.micropos.carts.repository;

import com.micropos.model.Cart;
import com.micropos.model.Item;

public interface CartRepository {
    Cart getCart();

    void addItem(Item item);

    void removeItem(Item item);

    void modifyItem(Item item);
}
