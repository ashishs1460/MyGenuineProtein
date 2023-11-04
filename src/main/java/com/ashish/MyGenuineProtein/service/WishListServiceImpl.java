package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.*;
import com.ashish.MyGenuineProtein.repository.WishListItemRepository;
import com.ashish.MyGenuineProtein.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListServiceImpl implements WishListService{
    @Autowired
    VariantService variantService;
    @Autowired
    UserService userService;
    @Autowired
    WishListRepository wishListRepository;
    @Autowired
    WishListItemRepository wishListItemRepository;


//    @Override
//    public int addToWishList(Long variantId, User user1) {
//        Variant variant = variantService.getVariantById(variantId).orElseThrow();
//        User user = userService.findUserByEmail(user1.getEmail()).orElseThrow();
//
//        WishList wishList = wishListRepository.findByUser(user).orElseGet(() -> createWishList(user));
//        System.out.println(wishList);
//        System.out.println(user);
//
//        List<WishListItem> wishListItems = wishListItemRepository.findByWishList(wishList);
//        System.out.println(wishListItems);
//
//        // Check if the product is already in the wishlist
//        for (WishListItem item : wishListItems) {
//            if (item.getVariant().equals(variant)) {
//                return 1; // Product already exists in the wishlist
//            }
//        }
//
//        // If the product is not in the wishlist, add it
//        WishListItem wishListItem = new WishListItem();
//        wishListItem.setVariant(variant);
//        wishListItem.setWishList(wishList);
//        wishListItemRepository.save(wishListItem);
//        return 2; // Product added to the wishlist
//    }
//
//    @Override
//    public List<WishListItem> getAll() {
//        return wishListItemRepository.findAll();
//    }
//
//    public WishList createWishList(User user) {
//        WishList wishList = new WishList();
//        wishList.setUser(user);
//        wishListRepository.save(wishList);
//        return wishList;
//    }
public int addToWishList(Long variantId, User user1) {
    Variant variant = variantService.getVariantById(variantId).orElseThrow();
    User user = userService.findUserByEmail(user1.getEmail()).orElseThrow();

    WishList wishList = wishListRepository.findByUser(user).orElseGet(() -> createWishList(user));

    // Ensure that the WishList is properly associated with the user
    user.setWishList(wishList);

    List<WishListItem> wishListItems = wishListItemRepository.findByWishList(wishList);

    // Check if the product is already in the wishlist
    for (WishListItem item : wishListItems) {
        if (item.getVariant().equals(variant)) {
            return 1; // Product already exists in the wishlist
        }
    }

    // If the product is not in the wishlist, add it
    WishListItem wishListItem = new WishListItem();
    wishListItem.setVariant(variant);
    wishListItem.setWishList(wishList);
    wishListItemRepository.save(wishListItem);
    return 2; // Product added to the wishlist
}

    public List<WishListItem> getAll() {
        return wishListItemRepository.findAll();
    }

//    @Override
//    public List<WishListItem> getWhishlistByUser(User user) {
//        return wishListItemRepository.findByUser(user);
//    }

    public WishList createWishList(User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);
        user.setWishList(wishList);
        wishListRepository.save(wishList);
        return wishList;
    }


}
