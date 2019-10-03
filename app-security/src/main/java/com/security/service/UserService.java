package com.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.model.User;
import com.security.repository.UserRepository;
import com.security.request.LoginUserDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {

	private final String USER_DOCUMENT_ID = "LoginUser:";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		List<User> users = userRepository.findByUsername(userName);
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	// Save New Login User
	public void saveNewLoginUser(LoginUserDetails loginUserDetails) {
		try {
			User user = new User();
			List<User> existUserInfo = userRepository.findStartsWithByOrderByIdDesc(USER_DOCUMENT_ID);
			user.setId(!existUserInfo.isEmpty()
					? USER_DOCUMENT_ID.concat(Integer.toString(
							(Integer.parseInt(existUserInfo.get(existUserInfo.size() - 1).getId().split(":")[1]) + 1)))
					: USER_DOCUMENT_ID.concat("1"));
			user.setUsername(loginUserDetails.getUsername());
			user.setPassword(passwordEncoder.encode(loginUserDetails.getPassword()));
			user.setEnabled(true);
			user.setAccountNonExpired(true);
			user.setAccountNonLocked(true);
			user.setCredentialsNonExpired(true);
			List<GrantedAuthority> grantAuthority = new ArrayList<>();
			for (String roles : loginUserDetails.getAuthorities()) {
				grantAuthority.add(new SimpleGrantedAuthority(roles));
			}
			user.setAuthorities(grantAuthority);
			userRepository.save(user);
		} catch (Exception ex) {
			log.info("##### Error while register new login user in method ---> saveNewLoginUser() ", ex);
		}
	}
}
