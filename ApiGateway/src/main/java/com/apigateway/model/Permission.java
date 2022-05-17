package com.apigateway.model;

import org.springframework.security.core.GrantedAuthority;


public class Permission implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;
	
	
    Long id;

    String name;
    
  
    
	public Permission() {
		super();
	}

	
	

	public Permission(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}




	@Override
	public String getAuthority() {
		return name;
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}
	
	

}
