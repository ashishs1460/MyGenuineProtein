package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Flavour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlavourRepository extends JpaRepository<Flavour, UUID> {
}
