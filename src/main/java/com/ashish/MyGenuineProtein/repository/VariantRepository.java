package com.ashish.MyGenuineProtein.repository;

import com.ashish.MyGenuineProtein.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {
    Optional<Variant> findById(Long id);
}
