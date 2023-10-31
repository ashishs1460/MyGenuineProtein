package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.CartItem;
import com.ashish.MyGenuineProtein.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem ,Long> {
    Optional<CartItem> findByCartAndVariant(Cart cart, Variant variant);

    void delete(CartItem cartItem);


}
