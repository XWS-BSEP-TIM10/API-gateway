package com.apigateway.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;


// POJO koji implementira Spring Security GrantedAuthority kojim se mogu definisati role u aplikaciji

public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;


    Long id;


    String name;
    
    private Set<Permission> permission = new HashSet<Permission>();

    

	public Role(Long id, String name, Set<com.apigateway.model.Permission> permission) {
		super();
		this.id = id;
		this.name = name;
		this.permission = permission;
	}

	@JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Set<Permission> getPermission() {
		return permission;
	}

	public void setPermission(Set<Permission> permission) {
		this.permission = permission;
	}
    
    

}
