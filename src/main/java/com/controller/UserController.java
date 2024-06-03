package com.controller;

import com.entity.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

	// Логика получения публичной информации о пользователях для неавторизованных пользователей
	@GetMapping("/public-info")
	public ResponseEntity<String> getPublicUserInfo() {
		// Здесь может быть ваша логика получения публичной информации о пользователях
		return ResponseEntity.ok("Public user information");
	}

	// Логика получения информации о пользователе по его идентификатору для авторизованных пользователей
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		User user = userService.read(id);
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		return ResponseEntity.ok(user);
	}

	// Логика добавления нового пользователя для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addUserForAdmin(@RequestBody User user) {
		userService.save(user);
		return ResponseEntity.ok("New user added");
	}

	// Логика удаления пользователя по его идентификатору для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUserForAdmin(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok("User with ID " + id + " deleted");
	}

	// Логика обновления информации о пользователе для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateUserForAdmin(@PathVariable Long id, @RequestBody User userData) {
		User existingUser = userService.read(id);
		if (existingUser == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		existingUser.setUsername(userData.getUsername());
		existingUser.setPassword(userData.getPassword());
		existingUser.setRole(userData.getRole());
		userService.edit(existingUser);
		return ResponseEntity.ok("Information about user with ID " + id + " updated");
	}
}
