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

    @Autowired
    UserService userService;

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

    @Override
    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

    @Override
    public void setDefaultAddress(Long addressId, User user) {
        Optional<Address> selectedAddressOptional = addressRepository.findById(addressId);

        if (selectedAddressOptional.isPresent()) {
            Address selectedAddress = selectedAddressOptional.get();

            List<Address> userAddresses = user.getAddresses();

            // Find the current default address (if any)
            Address currentDefaultAddress = userAddresses.stream()
                    .filter(address -> address.isDefault())
                    .findFirst()
                    .orElse(null);

            if (currentDefaultAddress != null) {
                // Unset the current default address
                currentDefaultAddress.setDefault(false);
                addressRepository.save(currentDefaultAddress);
            }

            // Set the selected address as the new default
            selectedAddress.setDefault(true);
            addressRepository.save(selectedAddress);
        }
    }





}
