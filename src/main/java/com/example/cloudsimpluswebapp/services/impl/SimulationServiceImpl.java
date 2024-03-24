package com.example.cloudsimpluswebapp.services.impl;

import com.example.cloudsimpluswebapp.dto.CloudletDTO;
import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.dto.SimulationResultDTO;
import com.example.cloudsimpluswebapp.models.enums.SimulationType;
import com.example.cloudsimpluswebapp.repositories.SimulationRepository;
import com.example.cloudsimpluswebapp.security.CurrentPersonResolver;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.example.cloudsimpluswebapp.simulations.BasicSimulation;
import com.example.cloudsimpluswebapp.simulations.CloudletAndVmLifeTimeSimulation;
import com.example.cloudsimpluswebapp.simulations.CloudletCancellationSimulation;
import com.example.cloudsimpluswebapp.utils.DateUtil;
import com.example.cloudsimpluswebapp.utils.exceptions.SimulationException;
import com.example.cloudsimpluswebapp.utils.mappers.SimulationMapper;
import com.example.cloudsimpluswebapp.utils.mappers.SimulationResultMapper;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SimulationServiceImpl implements SimulationService {

    private static final Logger log = LoggerFactory.getLogger(SimulationServiceImpl.class);
    private final SimulationRepository simulationRepository;
    private final BasicSimulation basicSimulation;
    private final CloudletAndVmLifeTimeSimulation cloudletAndVmLifeTimeSimulation;
    private final SimulationResultMapper simulationResultMapper;
    private final SimulationMapper simulationMapper;
    private final CurrentPersonResolver currentPersonResolver;
    private final CloudletCancellationSimulation cloudletCancellationSimulation;

    @Autowired
    public SimulationServiceImpl(SimulationRepository simulationRepository, BasicSimulation basicSimulation, SimulationResultMapper simulationResultMapper, SimulationMapper simulationMapper, CloudletAndVmLifeTimeSimulation cloudletAndVmLifeTimeSimulation, CurrentPersonResolver currentPersonResolver, CloudletCancellationSimulation cloudletCancellationSimulation) {
        this.simulationRepository = simulationRepository;
        this.basicSimulation = basicSimulation;
        this.simulationResultMapper = simulationResultMapper;
        this.simulationMapper = simulationMapper;
        this.cloudletAndVmLifeTimeSimulation = cloudletAndVmLifeTimeSimulation;
        this.currentPersonResolver = currentPersonResolver;
        this.cloudletCancellationSimulation = cloudletCancellationSimulation;
    }

    @Override
    public SimulationDTO startBasicSimulation(SimulationDTO simulationDTO) throws SimulationException {
        try {
            simulationDTO.setSimulationResultDTOS(
                    getSimulationResultListDTO(
                            basicSimulation.startBasicSimulation(simulationDTO),
                            simulationDTO.getCloudletDTOS()
                    )
            );

            if (simulationDTO.isSaveResults()){
                simulationRepository.save(simulationMapper.map(simulationDTO).setPerson(currentPersonResolver.getCurrentPerson()));
            }
        } catch (Exception e){
            throwException(e, simulationDTO);
        }
        return simulationDTO;
    }

    @Override
    public SimulationDTO startLifeTimeSimulation(SimulationDTO simulationDTO) throws SimulationException {
        try {
            simulationDTO.setSimulationResultDTOS(
                    getSimulationResultListDTO(
                            cloudletAndVmLifeTimeSimulation.startLifeTimeSimulation(simulationDTO),
                            simulationDTO.getCloudletDTOS()
                    )
            );

            if (simulationDTO.isSaveResults()){
                simulationRepository.save(simulationMapper.map(simulationDTO).setPerson(currentPersonResolver.getCurrentPerson()));
            }
        } catch (Exception e){
            throwException(e, simulationDTO);
        }
        return simulationDTO;
    }

    @Override
    public SimulationDTO startCloudletCancellationSimulation(SimulationDTO simulationDTO) throws SimulationException {
        //TODO Нужно доделать обработку ошибку после отмены решения задачи
        try{
            simulationDTO.setSimulationResultDTOS(
                    getSimulationResultListDTO(
                            cloudletCancellationSimulation.startCloudletCancellationSimulation(simulationDTO),
                            simulationDTO.getCloudletDTOS()
                    )
            );

            if (simulationDTO.isSaveResults()){
                simulationRepository.save(simulationMapper.map(simulationDTO).setPerson(currentPersonResolver.getCurrentPerson()));
            }
        } catch (Exception e){
            throwException(e, simulationDTO);
        }
        return simulationDTO;
    }

    @Override
    public List<SimulationDTO> getSimulationsByPerson() {
        return simulationRepository
                .getSimulationsByPerson(currentPersonResolver.getCurrentPerson())
                .stream()
                .map(simulationMapper::map)
                .map(simulationDTO -> simulationDTO
                        .setActionUrl(simulationDTO.getSimulationType()
                                .getUrl()))
                .toList();
    }

    @Override
    public Page<SimulationDTO> getSimulationsByPerson(int page) {
        Pageable pageable = PageRequest.of(page, 3);

        long totalSimulations = simulationRepository.countSimulationsByPerson(currentPersonResolver.getCurrentPerson());
        log.info("TEST CHECK: " + totalSimulations);
        List<SimulationDTO> simulationDTOS = simulationRepository
                .getSimulationsByPerson(currentPersonResolver.getCurrentPerson(), pageable)
                .stream()
                .map(simulationMapper::map)
                .map(simulationDTO -> simulationDTO
                        .setActionUrl(simulationDTO.getSimulationType()
                                .getUrl()))
                .toList();
        return new PageImpl<>(simulationDTOS, pageable, totalSimulations);
    }

    @Override
    public Page<SimulationDTO> getSimulationsByPersonAndFilter(int page, String nameSimulation, String dateOfCreation, String simulationType, boolean simulationRemoved) throws ParseException {
        Pageable pageable = PageRequest.of(page, 3);
        log.info("page: " + page
        + " nameSimulation: " + nameSimulation
        + " dateOfCreation: " + dateOfCreation
        + " simulationType: " + simulationType
        + " simulationRemoved: " + simulationRemoved);
        long totalSimulations = simulationRepository.countSimulationsByPersonAndNameSimulationAndDateOfCreationAndSimulationTypeAndSimulationRemoved(
                currentPersonResolver.getCurrentPerson(),
                nameSimulation,
                DateUtil.getDateFromString(dateOfCreation),
                SimulationType.getSimulationTypeByName(simulationType),
                simulationRemoved);

        List<SimulationDTO> simulationDTOS = simulationRepository
                .
        getSimulationsByPersonAndNameSimulationAndDateOfCreationAndSimulationTypeAndSimulationRemoved(currentPersonResolver.getCurrentPerson(),
                nameSimulation,
                DateUtil.getDateFromString(dateOfCreation),
                SimulationType.getSimulationTypeByName(simulationType),
                simulationRemoved,
                pageable)

                .stream()
                .map(simulationMapper::map)
                .map(simulationDTO -> simulationDTO
                        .setActionUrl(simulationDTO.getSimulationType()
                                .getUrl()))
                .toList();


        log.info("TEST CHECK: " + totalSimulations);
        return new PageImpl<>(simulationDTOS, pageable, totalSimulations);
    }

    @Override
    public SimulationDTO getSimulation(UUID simulationId) {
        return simulationMapper.map(simulationRepository.findById(simulationId).orElseThrow());
    }

    private List<SimulationResultDTO> getSimulationResultListDTO(List<Cloudlet> resultList, List<CloudletDTO> cloudletDTOS){
        List<SimulationResultDTO> simulationResultDTOS = new ArrayList<>();
        for (int i = 0, j = 0; i<cloudletDTOS.size(); i++){
            for (int k = 0; k<cloudletDTOS.get(i).getCloudletCount(); k++, j++){
                simulationResultDTOS.add(simulationResultMapper.map(resultList.get(j), cloudletDTOS.get(i)));
            }
        }
        //TODO придумать как реализовать цикл выше в потоках
/*        cloudletDTOS.stream()
                .flatMap(cloudletDTO -> {
                    int j = 0;
                    SimulationResultDTO simulationResultDTO;
                    IntStream.range(0, cloudletDTO.getCloudletCount()).mapToObj(k -> {
                        int f = j;
                        simulationResultDTOS.add(simulationResultMapper.map(resultList.get(j++), cloudletDTO));
                    });
                    return simulationResultDTO;
                })
                .forEach(simulationResultDTOS::add);*/
        return simulationResultDTOS;
    }

    private void throwException(Exception e, SimulationDTO simulationDTO) throws SimulationException {
        //TODO Подумать над обработкой ошибок
        log.error(String.format("Произошла ошибка при запуске симуляции с именем: %s", simulationDTO.getNameSimulation()));
        throw new SimulationException(e);
    }
}
