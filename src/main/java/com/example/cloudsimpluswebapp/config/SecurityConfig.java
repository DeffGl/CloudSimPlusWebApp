package com.example.cloudsimpluswebapp.config;

import com.example.cloudsimpluswebapp.services.impl.PersonCredentialDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PersonCredentialDetailService personCredentialDetailService;

    @Autowired
    public SecurityConfig(PersonCredentialDetailService personCredentialDetailService) {
        this.personCredentialDetailService = personCredentialDetailService;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personCredentialDetailService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setUseReferer(true);
        return handler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request) -> request.requestMatchers("/public/**",
                                "/auth/login", "/auth/registration", "/error", "/simulation", "/simulation/basic", "/simulation/lifetime", "/").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/auth/login")
                        .successHandler(successHandler())
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/simulation")
                        .failureUrl("/auth/login?error")
                        .permitAll())
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/logout").logoutSuccessUrl("/auth/login"));

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
