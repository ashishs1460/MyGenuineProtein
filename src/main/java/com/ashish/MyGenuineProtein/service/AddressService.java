package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.User;

import java.util.Arrays;
import java.util.List;

public interface AddressService {
    void addNewAddress(Address address, User user);

    List<Address>findAllUserAddresses(User user);
}
