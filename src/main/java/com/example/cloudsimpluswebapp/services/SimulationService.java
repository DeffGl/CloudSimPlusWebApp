package com.example.cloudsimpluswebapp.services;

import com.example.cloudsimpluswebapp.models.Simulation;
import org.cloudsimplus.cloudlets.Cloudlet;

import java.util.List;

public interface SimulationService {
    List<Cloudlet> simulationStart(Simulation simulation);
}
