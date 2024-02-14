package com.example.cloudsimpluswebapp.simulations;

import com.example.cloudsimpluswebapp.models.Simulation;
import org.cloudsimplus.cloudlets.Cloudlet;

import java.util.List;

public interface BasicSimulation {
    //TODO переделать на MODEL а не DTO
    List<Cloudlet> startSimulation(Simulation simulation);
}
