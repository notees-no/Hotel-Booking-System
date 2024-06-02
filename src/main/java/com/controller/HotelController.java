package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
public class HotelController {

	// Для неавторизованных
	@GetMapping("/{id}")
	public ResponseEntity<String> getHotelByIdForUnauthorized(@PathVariable Long id) {
		// Логика получения информации об отеле для неавторизованных
		return ResponseEntity.ok("Information about hotel with ID " + id);
	}

	// Для неавторизованных
	@GetMapping("/name/{name}")
	public ResponseEntity<String> getHotelsByNameForUnauthorized(@PathVariable String name) {
		// Логика получения списка отелей по названию для неавторизованных
		return ResponseEntity.ok("List of hotels with name " + name);
	}

	// Для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addHotelForAdmin(@RequestBody String hotelData) {
		// Логика добавления нового отеля для администраторов
		return ResponseEntity.ok("New hotel added");
	}

	// Для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteHotelForAdmin(@PathVariable Long id) {
		// Логика удаления отеля для администраторов
		return ResponseEntity.ok("Hotel with ID " + id + " deleted");
	}

	// Для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateHotelForAdmin(@PathVariable Long id, @RequestBody String hotelData) {
		// Логика обновления информации об отеле для администраторов
		return ResponseEntity.ok("Information about hotel with ID " + id + " updated");
	}
}
