package com.example.cloudsimpluswebapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonCredentialDTO {

    @NotEmpty(message = "Поле с логином не должно быть пустым")
    @Size(min=2, max=100, message = "Логин должен быть длинной от 2 до 100 символов")
    private String username;
    @NotEmpty(message = "Поле с паролем не должно быть пустым")
    private String password;
    private PersonDTO personDTO;
}
