package com.example.shop.member.application.dto;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class LoginAuthentication extends UsernamePasswordAuthenticationToken {

    public LoginAuthentication(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public LoginAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }


}
