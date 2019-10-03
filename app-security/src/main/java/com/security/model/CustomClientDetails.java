package com.security.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Document
@Data
@Getter
@Setter
public class CustomClientDetails implements ClientDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6567871335341816994L;
	
    @Id
    private String id;

    @Field
    private String clientId;
    @Field
    private Set<String> resourceIds;
    @Field
    private boolean secretRequired;
    @Field
    private String clientSecret;
    @Field
    private boolean scoped;
    @Field
    private Set<String> scope;
    @Field
    private Set<String> authorizedGrantTypes;
    @Field
    private Set<String> registeredRedirectUri;
    @Field
    private Collection<GrantedAuthority> authorities = Collections.emptySet();
    @Field
    private Integer accessTokenValiditySeconds;
    @Field
    private Integer refreshTokenValiditySeconds;
    @Field
    private boolean autoApprove;
    @Field
    private Map<String, Object> additionalInformation;
    @Field
    private Set<String> autoApproveScopes;
    
	@Override
	public boolean isAutoApprove(String scope) {
		if (autoApproveScopes == null) {
			return false;
		}
		for (String auto : autoApproveScopes) {
			if (auto.equals("true") || scope.matches(auto)) {
				return true;
			}
		}
		return false;
	}
	
}
