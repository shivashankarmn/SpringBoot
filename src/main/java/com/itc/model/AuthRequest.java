package com.itc.model;

public class AuthRequest {

	private String userName;
	private String password;
	
	public String getUsername() {
		return userName;
	}
	public void setUsername(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public AuthRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AuthRequest(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	

	
	
}
