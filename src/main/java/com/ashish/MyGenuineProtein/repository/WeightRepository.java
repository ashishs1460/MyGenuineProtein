package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WeightRepository extends JpaRepository<Weight, UUID> {

}
