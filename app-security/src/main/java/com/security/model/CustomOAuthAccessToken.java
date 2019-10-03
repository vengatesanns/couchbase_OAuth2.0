package com.security.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.security.converter.SerializableObjectConverter;

import lombok.Data;
import lombok.Getter;

@Document
@Data
@Getter
public class CustomOAuthAccessToken implements OAuth2AccessToken {


	@Id
	private String id;

	@Field
	@NotNull
	private String tokenId;
	@Field
	@NotNull
	private OAuth2AccessToken token;
	@Field
	private String refreshToken;
	@Field
	@NotNull
	private String authenticationId;
	@Field
	@NotNull
	private String username;
	@Field
	@NotNull
	private String clientId;
	@Field
	@NotNull
	private String authentication;
	@Field
	private String tokenType;
	@Field
	private boolean isExpired;
	@Field
	private Date expiration;
	@Field
	private int expiresIn;
	@Field
	private String value;
	@Field
	private Map<String, Object> additionalInformation;
	@Field
	private Set<String> scope;

	public OAuth2Authentication getAuthentication() {
		return SerializableObjectConverter.deserialize(authentication);
	}

	public void setAuthentication(OAuth2Authentication authentication) {
		this.authentication = SerializableObjectConverter.serialize(authentication);
	}

	public OAuth2RefreshToken getRefreshToken() {
		return null;
	}
}
