package com.security.request;

import lombok.Data;

@Data
public class LoginUserDetails {
	private String username;
	private String password;
	private String[] authorities;
}
