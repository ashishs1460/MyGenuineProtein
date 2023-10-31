package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address , Long> {

    List<Address> findAllByUser(User user);
}
