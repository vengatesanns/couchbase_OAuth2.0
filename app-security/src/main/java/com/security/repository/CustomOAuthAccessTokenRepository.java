package com.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;

import com.security.model.CustomOAuthAccessToken;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "CustomOAuthAccessToken")
public interface CustomOAuthAccessTokenRepository extends CouchbasePagingAndSortingRepository<CustomOAuthAccessToken, String> {

    List<CustomOAuthAccessToken> findByClientId(String clientId);

    List<CustomOAuthAccessToken> findByClientIdAndUsername(String clientId, String username);

    Optional<CustomOAuthAccessToken> findByTokenId(String tokenId);

    Optional<CustomOAuthAccessToken> findByRefreshToken(String refreshToken);

    Optional<CustomOAuthAccessToken> findByAuthenticationId(String authenticationId);
}
