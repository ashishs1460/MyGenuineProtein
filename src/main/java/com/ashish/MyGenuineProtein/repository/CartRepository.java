package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Cart;
import com.ashish.MyGenuineProtein.model.CartItems;
import com.ashish.MyGenuineProtein.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
  Optional<Cart>   findByUser(User user);



}
