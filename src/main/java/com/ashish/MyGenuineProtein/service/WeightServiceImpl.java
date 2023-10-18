package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Weight;
import com.ashish.MyGenuineProtein.repository.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WeightServiceImpl implements WeightService{

    @Autowired
    WeightRepository weightRepository;
    @Override
    public void addWeight(Weight weight) {
        weightRepository.save(weight);

    }

    @Override
    public List<Weight> getAllWeights() {
        return weightRepository.findAll();
    }

    @Override
    public Optional<Weight> getweightById(UUID weightId) {
        return weightRepository.findById(weightId);
    }
}
