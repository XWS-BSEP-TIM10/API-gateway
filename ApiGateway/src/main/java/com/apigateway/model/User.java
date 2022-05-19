package com.apigateway.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;


    private String username;

 
    private String password;

 
    private List<Role> roles;


    public User() {
    }


    public User(String uuid, String username, String password, List<Role> userType) {
        super();
        this.id = uuid;
        this.username = username;
        this.password = password;
        this.roles = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Permission> permissions = new HashSet<Permission>();
        for(Role role : this.roles){
            for(Permission permission : role.getPermission()){
                permissions.add(permission);
            }
        }
        return permissions;
    }
}
