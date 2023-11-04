package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    Optional<WishList> findByUser(User user);
}
