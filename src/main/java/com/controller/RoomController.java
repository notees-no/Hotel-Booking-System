package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class RoomController {

	// Для пользователей
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> getRoomByIdForUser(@PathVariable Long id) {
		// Логика получения информации о номере для пользователей
		return ResponseEntity.ok("Information about room with ID " + id);
	}

	// Для пользователей
	@GetMapping("/number/{roomNumber}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> getRoomsByNumberForUser(@PathVariable String roomNumber) {
		// Логика получения списка номеров по номеру для пользователей
		return ResponseEntity.ok("List of rooms with number " + roomNumber);
	}

	// Для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addRoomForAdmin(@RequestBody String roomData) {
		// Логика добавления нового номера для администраторов
		return ResponseEntity.ok("New room added");
	}

	// Для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteRoomForAdmin(@PathVariable Long id) {
		// Логика удаления номера для администраторов
		return ResponseEntity.ok("Room with ID " + id + " deleted");
	}

	// Для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateRoomForAdmin(@PathVariable Long id, @RequestBody String roomData) {
		// Логика обновления информации о номере для администраторов
		return ResponseEntity.ok("Information about room with ID " + id + " updated");
	}
}
