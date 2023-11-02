package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
