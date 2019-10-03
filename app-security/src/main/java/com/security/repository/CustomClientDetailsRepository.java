package com.security.repository;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import com.security.model.CustomClientDetails;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "CustomClientDetails")
public interface CustomClientDetailsRepository extends CouchbaseRepository<CustomClientDetails, String> {

	CustomClientDetails findByClientId(String clientId);
}
