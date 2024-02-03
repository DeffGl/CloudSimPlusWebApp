package com.example.cloudsimpluswebapp.security;

import com.example.cloudsimpluswebapp.models.PersonCredential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PersonCredentialDetails implements UserDetails {

    private final PersonCredential personCredential;

    public PersonCredentialDetails(PersonCredential personCredential) {
        this.personCredential = personCredential;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return personCredential.getPassword();
    }

    @Override
    public String getUsername() {
        return personCredential.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
