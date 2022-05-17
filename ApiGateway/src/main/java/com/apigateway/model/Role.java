package com.apigateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;


// POJO koji implementira Spring Security GrantedAuthority kojim se mogu definisati role u aplikaciji

public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;


    Long id;


    String name;
    
    

    public Role(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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

}
