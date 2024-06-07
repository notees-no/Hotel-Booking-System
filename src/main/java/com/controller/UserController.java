package com.controller;

import com.entity.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

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
	public ResponseEntity<Map<String, String>> getPublicUserInfo() {
		Map<String, String> response = new HashMap<>();
		response.put("message", "Public user information");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Логика получения информации о пользователе по его идентификатору для авторизованных пользователей
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		User user = userService.read(id);
		if (user == null) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "User not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// Логика добавления нового пользователя для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, String>> addUserForAdmin(@RequestBody User user) {
		userService.save(user);
		Map<String, String> response = new HashMap<>();
		response.put("message", "New user added");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Логика удаления пользователя по его идентификатору для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, String>> deleteUserForAdmin(@PathVariable Long id) {
		boolean isDeleted = userService.delete(id);
		if (!isDeleted) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "User not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		Map<String, String> response = new HashMap<>();
		response.put("message", "User with ID " + id + " deleted");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Логика обновления информации о пользователе для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, String>> updateUserForAdmin(@PathVariable Long id, @RequestBody User userData) {
		User existingUser = userService.read(id);
		if (existingUser == null) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "User not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		existingUser.setUsername(userData.getUsername());
		existingUser.setPassword(userData.getPassword());
		existingUser.setRole(userData.getRole());
		userService.edit(existingUser);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Information about user with ID " + id + " updated");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/count")
	public ResponseEntity<Long> getUserCount() {
        long count = userService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
	}
}
