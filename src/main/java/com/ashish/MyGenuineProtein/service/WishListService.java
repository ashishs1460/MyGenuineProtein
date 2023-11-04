package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.model.WishList;
import com.ashish.MyGenuineProtein.model.WishListItem;

import java.util.List;

public interface WishListService {
    int addToWishList(Long variantId, User user);

    List<WishListItem> getAll();

//    List<WishListItem> getWhishlistByUser(User user);
}
