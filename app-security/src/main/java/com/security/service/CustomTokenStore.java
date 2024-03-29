package com.security.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.security.model.CustomOAuthAccessToken;
import com.security.model.CustomOAuthRefreshToken;
import com.security.repository.CustomOAuthAccessTokenRepository;
import com.security.repository.CustomOAuthRefreshTokenRepository;

@Service
public class CustomTokenStore implements TokenStore {

	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

	@Autowired
	private CustomOAuthAccessTokenRepository cbAccessTokenRepository;

	@Autowired
	private CustomOAuthRefreshTokenRepository cbRefreshTokenRepository;

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
		List<CustomOAuthAccessToken> result = cbAccessTokenRepository.findByClientId(clientId);
		result.forEach(e -> tokens.add(e.getToken()));
		return tokens;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
		List<CustomOAuthAccessToken> result = cbAccessTokenRepository.findByClientIdAndUsername(clientId, userName);
		result.forEach(e -> tokens.add(e.getToken()));
		return tokens;
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		OAuth2AccessToken accessToken = null;
		String authenticationId = authenticationKeyGenerator.extractKey(authentication);
		Optional<CustomOAuthAccessToken> token = cbAccessTokenRepository.findByAuthenticationId(authenticationId);

		if (token.isPresent()) {
			accessToken = token.get().getToken();
			if (accessToken != null && !authenticationId
					.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken)))) {
				this.removeAccessToken(accessToken);
				this.storeAccessToken(accessToken, authentication);
			}
		}
		return accessToken;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		Optional<CustomOAuthAccessToken> accessToken = cbAccessTokenRepository
				.findByTokenId(extractTokenKey(tokenValue));
		if (accessToken.isPresent()) {
			return accessToken.get().getToken();
		}
		return null;
	}

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
		return readAuthentication(accessToken.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String tokenValues) {
		Optional<CustomOAuthAccessToken> accessToken = cbAccessTokenRepository
				.findByTokenId(extractTokenKey(tokenValues));
		if (accessToken.isPresent()) {
			return accessToken.get().getAuthentication();
		}
		return null;
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
		Optional<CustomOAuthRefreshToken> rtk = cbRefreshTokenRepository
				.findByTokenId(extractTokenKey(refreshToken.getValue()));
		return rtk.isPresent() ? rtk.get().getAuthentication() : null;
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		Optional<CustomOAuthRefreshToken> refreshToken = cbRefreshTokenRepository
				.findByTokenId(extractTokenKey(tokenValue));
		return refreshToken.isPresent() ? refreshToken.get().getToken() : null;
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken accessToken) {
		Optional<CustomOAuthAccessToken> accessTokenToRemove = cbAccessTokenRepository
				.findByTokenId(extractTokenKey(accessToken.getValue()));
		if (accessTokenToRemove.isPresent()) {
			cbAccessTokenRepository.delete(accessTokenToRemove.get());
		}
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		Optional<CustomOAuthAccessToken> token = cbAccessTokenRepository
				.findByRefreshToken(extractTokenKey(refreshToken.getValue()));
		if (token.isPresent()) {
			cbAccessTokenRepository.delete(token.get());
		}
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
		Optional<CustomOAuthRefreshToken> rtk = cbRefreshTokenRepository
				.findByTokenId(extractTokenKey(refreshToken.getValue()));
		if (rtk.isPresent()) {
			cbRefreshTokenRepository.delete(rtk.get());
		}
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		String refreshToken = null;
		if (accessToken.getRefreshToken() != null) {
			refreshToken = accessToken.getRefreshToken().getValue();
		}

		if (readAccessToken(accessToken.getValue()) != null) {
			this.removeAccessToken(accessToken);
		}

		CustomOAuthAccessToken cat = new CustomOAuthAccessToken();
		cat.setId(UUID.randomUUID().toString() + UUID.randomUUID().toString());
		cat.setTokenId(extractTokenKey(accessToken.getValue()));
		cat.setToken(accessToken);
		cat.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
		cat.setUsername(authentication.isClientOnly() ? null : authentication.getName());
		cat.setClientId(authentication.getOAuth2Request().getClientId());
		cat.setAuthentication(authentication);
		cat.setRefreshToken(extractTokenKey(refreshToken));

		cbAccessTokenRepository.save(cat);
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		CustomOAuthRefreshToken crt = new CustomOAuthRefreshToken();
		crt.setId(UUID.randomUUID().toString() + UUID.randomUUID().toString());
		crt.setTokenId(extractTokenKey(refreshToken.getValue()));
		crt.setToken(refreshToken);
		crt.setAuthentication(authentication);
		cbRefreshTokenRepository.save(crt);
	}

	private String extractTokenKey(String value) {
		if (value == null) {
			return null;
		} else {
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException var5) {
				throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
			}

			try {
				byte[] e = digest.digest(value.getBytes("UTF-8"));
				return String.format("%032x", new Object[] { new BigInteger(1, e) });
			} catch (UnsupportedEncodingException var4) {
				throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
			}
		}
	}
}
