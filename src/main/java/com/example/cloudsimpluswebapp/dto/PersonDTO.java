package com.example.cloudsimpluswebapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PersonDTO {
    @NotEmpty(message = "Поле с именем не должно быть пустым")
    private String name;

    @NotEmpty(message="Поле с почтой не должно быть пустым")
    @Email(message = "Почта должна быть валидной")
    private String email;

    private List<SimulationDTO> simulationDTOS;
}
