package com.service;

import com.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends Service<User> {
	void create(User user);
	User getByUsername(String username);
	UserDetailsService userDetailsService();
}
