package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Address;
import com.ashish.MyGenuineProtein.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address , Long> {

    List<Address> findAllByUser(User user);
}
