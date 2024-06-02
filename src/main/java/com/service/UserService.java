package com.service;

import com.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends Service<User>{
	User getByUsername(String username);
	UserDetailsService userDetailsService();
}