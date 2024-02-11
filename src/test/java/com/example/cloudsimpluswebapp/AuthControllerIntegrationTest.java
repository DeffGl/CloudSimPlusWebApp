package com.example.cloudsimpluswebapp;

import com.example.cloudsimpluswebapp.dto.PersonCredentialDTO;
import com.example.cloudsimpluswebapp.dto.PersonDTO;
import com.example.cloudsimpluswebapp.services.PersonCredentialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonCredentialService personCredentialService;

    @Test
    public void testRegistrationSuccess() throws Exception {

        // Prepare registration data with invalid input
        PersonCredentialDTO registrationData = new PersonCredentialDTO("testuser", "testpassword", new PersonDTO("John Doe", "john@example.com"));

        // Perform registration
        mockMvc.perform(post("/auth/registration")
                        .param("username", registrationData.getUsername())
                        .param("name", registrationData.getPersonDTO().getName())
                        .param("email", registrationData.getPersonDTO().getEmail())
                        .param("password", registrationData.getPassword())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    //TODO Доделать этот тест
    /*@ParameterizedTest
    @CsvSource({
            "'', name, asd@mail.ru, testpassword", // Valid input
            "testuser, '', asd@mail.ru, testpassword", // Valid input
            "testuser, name, '', testpassword", // Valid input
            "testuser, name, asd@mail.ru, '' " // Empty password
            // Добавьте дополнительные комбинации данных, если необходимо
    })
    public void testRegistrationFailure(String username, String name, String email, String password) throws Exception {
        // Prepare registration data with invalid input
        PersonCredentialDTO registrationData = new PersonCredentialDTO(username, password, new PersonDTO(name, email));

        // Perform registration attempt
        mockMvc.perform(post("/auth/registration")
                        .param("username", registrationData.getUsername())
                        .param("name", registrationData.getPersonDTO().getName())
                        .param("email", registrationData.getPersonDTO().getEmail())
                        .param("password", registrationData.getPassword())
                        .with(csrf()))
                .andExpect(status().isOk()) // Expect registration page because of validation errors
                .andExpect(model().attributeHasFieldErrors("personCredentialDTO", "username", "personDTO.name", "personDTO.email", "password")); // Expect validation error for password field
    }*/
}
