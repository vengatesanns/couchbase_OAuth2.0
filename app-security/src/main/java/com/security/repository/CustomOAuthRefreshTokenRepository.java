package com.security.repository;

import java.util.Optional;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import com.security.model.CustomOAuthRefreshToken;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "CustomOAuthRefreshToken")
public interface CustomOAuthRefreshTokenRepository extends CouchbaseRepository<CustomOAuthRefreshToken, String> {

    Optional<CustomOAuthRefreshToken> findByTokenId(String tokenId);
}
