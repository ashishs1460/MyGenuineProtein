package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
