package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.PersonCredentialDTO;
import com.example.cloudsimpluswebapp.services.PersonCredentialService;
import com.example.cloudsimpluswebapp.utils.mappers.PersonCredentialMapper;
import com.example.cloudsimpluswebapp.utils.validators.PersonCredentialsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonCredentialsValidator personCredentialsValidator;
    private final PersonCredentialService personCredentialService;
    private final PersonCredentialMapper personCredentialMapper;

    @Autowired
    public AuthController(PersonCredentialsValidator personCredentialsValidator, PersonCredentialService personCredentialService, PersonCredentialMapper personCredentialMapper) {
        this.personCredentialsValidator = personCredentialsValidator;
        this.personCredentialService = personCredentialService;
        this.personCredentialMapper = personCredentialMapper;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("personCredentialDTO") PersonCredentialDTO personCredentialDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("personCredentialDTO") @Validated PersonCredentialDTO personCredentialDTO, BindingResult bindingResult) {
        personCredentialsValidator.validate(personCredentialDTO, bindingResult);

        if (bindingResult.hasErrors()){
            return "auth/registration";
        }
        personCredentialService.registerCredential(personCredentialMapper.map(personCredentialDTO));
        return "redirect:auth/login";
    }

}
