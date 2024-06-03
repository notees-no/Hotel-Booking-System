package com.controller;

import com.entity.User;
import com.enums.Role;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<User> getUserByIdForUser(@PathVariable Long id) {
		User user = userService.read(id);
		return checkEntityAndRole(user);
	}

	@GetMapping("/username/{username}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<User> getUserByUsernameForUser(@PathVariable String username) {
		User user = userService.getByUsername(username);
		return checkEntityAndRole(user);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> addUserForAdmin(@RequestBody User user) {
		userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUserForAdmin(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok("User with ID " + id + " deleted");
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> updateUserForAdmin(@PathVariable Long id, @RequestBody User userData) {
		User existingUser = userService.read(id);
		if (existingUser == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		existingUser.setUsername(userData.getUsername());
		existingUser.setPassword(userData.getPassword());
		existingUser.setRole(userData.getRole());
		userService.edit(existingUser);
		return ResponseEntity.ok(existingUser);
	}

	private ResponseEntity<User> checkEntityAndRole(User user) {
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		if (currentPrincipalName.equals(user.getUsername()) || user.getRole().equals(Role.ROLE_ADMIN)) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
}
