package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

	// Для неавторизованных пользователей
	@GetMapping("/public-info")
	public ResponseEntity<String> getPublicUserInfo() {
		// Логика получения публичной информации о пользователях для неавторизованных пользователей
		return ResponseEntity.ok("Public user information");
	}

	// Для авторизованных пользователей
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> getUserById(@PathVariable Long id) {
		// Логика получения информации о пользователе по его идентификатору для авторизованных пользователей
		return ResponseEntity.ok("Information about user with ID " + id);
	}

	// Для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addUserForAdmin(@RequestBody String userData) {
		// Логика добавления нового пользователя для администраторов
		return ResponseEntity.ok("New user added");
	}

	// Для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUserForAdmin(@PathVariable Long id) {
		// Логика удаления пользователя по его идентификатору для администраторов
		return ResponseEntity.ok("User with ID " + id + " deleted");
	}

	// Для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateUserForAdmin(@PathVariable Long id, @RequestBody String userData) {
		// Логика обновления информации о пользователе для администраторов
		return ResponseEntity.ok("Information about user with ID " + id + " updated");
	}
}
