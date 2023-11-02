package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems,Long> {

}
