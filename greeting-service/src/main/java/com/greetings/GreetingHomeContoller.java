package com.greetings;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingHomeContoller {

	@GetMapping("/get-welcome-message-admin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String getWelcomeMsgForOnlyAdmin() {
		return "Hi Admin, Welcome to access the Protected Resourcessss!!!";
	}

	@GetMapping("/get-welcome-message-all")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
	public String getWelcomeMsgForAll() {
		return "Hi All, Welcome to access the Protected Resourcessss!!!";
	}
}
