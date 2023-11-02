package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.CartItems;
import com.ashish.MyGenuineProtein.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems,Long> {
    Optional<CartItems> findByCartAndVariant(Cart cart, Variant variant);

    void delete(CartItems cartItems);


}
