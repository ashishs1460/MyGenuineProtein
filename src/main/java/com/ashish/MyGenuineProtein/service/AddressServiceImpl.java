package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.User;
import com.ashish.MyGenuineProtein.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    AddressRepository addressRepository;

    @Override
    public void addNewAddress(Address incommingAddress, User user) {
        Address address = new Address();
        address.setStreetAddress(incommingAddress.getStreetAddress());
        address.setCity(incommingAddress.getCity());
        address.setState(incommingAddress.getState());
        address.setPinCode(incommingAddress.getPinCode());
        address.setLandmark(incommingAddress.getLandmark());
        address.setCreatedAt(LocalDate.now());
        address.setDelete(false);
        address.setUser(user);
        addressRepository.save(address);
    }

    @Override
    public List<Address> findAllUserAddresses(User user) {
        return addressRepository.findAllByUser(user);
    }

    @Override
    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public void editAddress( Address updatedaddress) {
        addressRepository.save(updatedaddress);
    }


}
