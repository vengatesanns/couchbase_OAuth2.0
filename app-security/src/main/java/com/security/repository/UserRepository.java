package com.security.repository;

import java.util.List;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import com.security.model.User;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "User")
public interface UserRepository extends CouchbaseRepository<User, Long> {

	List<User> findByUsername(String username);

//	Optional<User> findTopStartsWithByOrderByIdDesc(String id);

	List<User> findStartsWithByOrderByIdDesc(String id);
}
