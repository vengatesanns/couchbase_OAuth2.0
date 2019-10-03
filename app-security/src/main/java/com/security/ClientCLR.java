package com.security;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.security.model.CustomClientDetails;
import com.security.repository.CustomClientDetailsRepository;

@Component
public class ClientCLR implements CommandLineRunner {

	@Autowired
	private CustomClientDetailsRepository customClientDetailsRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... strings) throws Exception {
		createOauthClients();
	}

	private static final String AUTHORIZATION_CODE = "authorization_code";
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String PASSWORD = "password";
	private static final String IMPLICIT = "implicit";
	private static final String SCOPE_READ = "read";
	private static final String SCOPE_WRITE = "write";
	private static final String TRUST = "trust";
	private static final String DOCUMENT_ID = "oauth_client_details:my_server1";
	private static final String CLIENT_ID = "my_client_id";
	private static final String CLIENT_SECRET = "my_password";

	private void createOauthClients() {

		CustomClientDetails client1 = new CustomClientDetails();
		client1.setResourceIds(new HashSet<>(Arrays.asList("greeting_resource")));
		client1.setId(DOCUMENT_ID);
		client1.setClientId(CLIENT_ID);
		client1.setClientSecret(passwordEncoder.encode(CLIENT_SECRET));
		client1.setAuthorizedGrantTypes(
				new HashSet<>(Arrays.asList(AUTHORIZATION_CODE, REFRESH_TOKEN, PASSWORD, IMPLICIT)));
		client1.setScope(new HashSet<String>(Arrays.asList(SCOPE_READ, SCOPE_WRITE, TRUST)));
		client1.setSecretRequired(true);
		client1.setAccessTokenValiditySeconds(3600);
		client1.setRefreshTokenValiditySeconds(3600);
		client1.setScoped(false);
		client1.setAutoApprove(false);
		client1.setRegisteredRedirectUri(new HashSet<String>());

		customClientDetailsRepository.save(client1);
	}
}
