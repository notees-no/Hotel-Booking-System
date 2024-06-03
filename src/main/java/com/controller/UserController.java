package com.controller;

import com.entity.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/public-info")
	public ResponseEntity<?> getPublicUserInfo() {
		// Здесь может быть ваша логика получения публичной информации о пользователях
		return ResponseEntity.ok("Public user information");
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		User user = userService.read(id);
		if (user == null) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(user);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addUserForAdmin(@RequestBody User user) {
		userService.save(user);
		return new ResponseEntity<>("New user added", HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUserForAdmin(@PathVariable Long id) {
		userService.delete(id);
		return new ResponseEntity<>("User with ID " + id + " deleted", HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateUserForAdmin(@PathVariable Long id, @RequestBody User userData) {
		User existingUser = userService.read(id);
		if (existingUser == null) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
		existingUser.setUsername(userData.getUsername());
		existingUser.setPassword(userData.getPassword());
		existingUser.setRole(userData.getRole());
		userService.edit(existingUser);
		return new ResponseEntity<>("Information about user with ID " + id + " updated", HttpStatus.OK);
	}
}
