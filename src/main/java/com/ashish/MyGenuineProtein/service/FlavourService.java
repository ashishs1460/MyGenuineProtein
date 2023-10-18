package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Flavour;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlavourService {

    void addFlavour(Flavour flavour);

    List<Flavour> getAllFlavours();

   Optional< Flavour> getFlavourById(UUID id);
}
