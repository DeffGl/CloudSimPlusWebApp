package com.example.cloudsimpluswebapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    @NotEmpty(message = "Поле с именем не должно быть пустым")
    private String name;

    @NotEmpty(message="Поле с почтой не должно быть пустым")
    @Email(message = "Почта должна быть валидной")
    private String email;

    private List<SimulationDTO> simulationDTOS;

    public PersonDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
