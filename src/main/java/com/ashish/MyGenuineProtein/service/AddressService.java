package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.User;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    void addNewAddress(Address address, User user);

    List<Address>findAllUserAddresses(User user);

    Optional<Address> findById(Long id);

    void editAddress(Address address);
}
