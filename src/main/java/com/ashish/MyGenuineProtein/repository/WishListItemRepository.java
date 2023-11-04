package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.WishList;
import com.ashish.MyGenuineProtein.model.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListItemRepository extends JpaRepository<WishListItem , Long> {


    List<WishListItem> findByWishList(WishList wishList);


}
