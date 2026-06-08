package com.vaishnav.airlinq.user.security;

import com.vaishnav.airlinq.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class UserDetailsImpl implements UserDetails {
    private String password;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String password, String email, Collection<? extends GrantedAuthority> authorities) {
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user){
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        return new UserDetailsImpl(
                user.getPassword(),
                user.getEmail(),
                Collections.singletonList(authority)
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
