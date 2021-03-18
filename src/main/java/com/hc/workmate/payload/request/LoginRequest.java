package com.hc.workmate.payload.request;

public class LoginRequest {

	private String username;
	private String password;
	private Long tenantOrClientId;

	public LoginRequest(String username, String password, Long tenantOrClientId) {
		this.username = username;
		this.password = password;
		this.tenantOrClientId = tenantOrClientId;
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

	public Long getTenantOrClientId() {
		return tenantOrClientId;
	}

	public void setTenantOrClientId(Long tenantOrClientId) {
		this.tenantOrClientId = tenantOrClientId;
	}
}