package com.security.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Document
@Data
@Getter
@Setter
public class User implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = -856973164173468798L;

	@Id
    private String id;
	
	@Field
    private String username;
	@Field
    private String password;
	@Field
    private Collection<GrantedAuthority> authorities = Collections.emptySet();
	@Field
    private boolean accountNonExpired;
	@Field
    private boolean accountNonLocked;
	@Field
    private boolean credentialsNonExpired;
	@Field
    private boolean enabled;
}
