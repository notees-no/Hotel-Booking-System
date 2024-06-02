package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	// Для пользователей
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> getReservationByIdForUser(@PathVariable Long id) {
		// Логика получения информации о бронировании для пользователей
		return ResponseEntity.ok("Information about reservation with ID " + id);
	}

	// Для пользователей
	@GetMapping("/status/{status}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> getReservationsByStatusForUser(@PathVariable String status) {
		// Логика получения списка бронирований по статусу для пользователей
		return ResponseEntity.ok("List of reservations with status " + status);
	}

	// Для пользователей
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> addReservationForUser(@RequestBody String reservationData) {
		// Логика добавления нового бронирования для пользователей
		return ResponseEntity.ok("New reservation added");
	}

	// Для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteReservationForAdmin(@PathVariable Long id) {
		// Логика удаления бронирования для администраторов
		return ResponseEntity.ok("Reservation with ID " + id + " deleted");
	}

	// Для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateReservationForAdmin(@PathVariable Long id, @RequestBody String reservationData) {
		// Логика обновления информации о бронировании для администраторов
		return ResponseEntity.ok("Information about reservation with ID " + id + " updated");
	}
}
