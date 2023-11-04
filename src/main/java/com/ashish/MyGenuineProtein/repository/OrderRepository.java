package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Order;
import com.ashish.MyGenuineProtein.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);


}
