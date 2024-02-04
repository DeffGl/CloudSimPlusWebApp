package com.example.cloudsimpluswebapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonDTO {
    @NotEmpty(message = "Поле с именем не должно быть пустым")
    private String name;
}
