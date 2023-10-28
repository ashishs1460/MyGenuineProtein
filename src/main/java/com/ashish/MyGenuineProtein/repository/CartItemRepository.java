package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem ,Long> {

}
