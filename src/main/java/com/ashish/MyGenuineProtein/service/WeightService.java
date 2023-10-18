package com.ashish.MyGenuineProtein.service;

import com.ashish.MyGenuineProtein.model.Weight;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeightService {

    void addWeight(Weight weight);

    List<Weight> getAllWeights();

  Optional<Weight>  getweightById(UUID weightId);
}
