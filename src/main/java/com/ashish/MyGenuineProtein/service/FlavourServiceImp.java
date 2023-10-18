package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Flavour;
import com.ashish.MyGenuineProtein.repository.FlavourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FlavourServiceImp implements FlavourService{

    @Autowired
    FlavourRepository flavourRepository;
    @Override
    public void addFlavour(Flavour flavour) {

        flavourRepository.save(flavour);

    }

    @Override
    public List<Flavour> getAllFlavours() {
        return flavourRepository.findAll();
    }

    @Override
    public Optional<Flavour> getFlavourById(UUID id) {
        return flavourRepository.findById(id);
    }
}
