package com.controller;

import com.entity.Reservation;
import com.enums.ReservationStatus;
import com.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	private final ReservationService reservationService;

	@Autowired
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	// Логика получения информации о бронировании для пользователей
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getReservationByIdForUser(@PathVariable Long id) {
		Reservation reservation = reservationService.read(id);
		if (reservation == null) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Reservation not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(reservation, HttpStatus.OK);
	}

	// Логика получения списка бронирований по статусу для пользователей
	@GetMapping("/status/{status}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getReservationsByStatusForUser(@PathVariable String status) {
		List<Reservation> reservations = reservationService.readByStatus(ReservationStatus.valueOf(status));
		if (reservations.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Reservations not found with status: " + status);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(reservations, HttpStatus.OK);
	}

	// Логика добавления нового бронирования для пользователей
	@PostMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> addReservationForUser(@RequestBody Reservation reservation) {
		reservationService.save(reservation);
		Map<String, String> response = new HashMap<>();
		response.put("message", "New reservation added");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Логика удаления бронирования для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteReservationForAdmin(@PathVariable Long id) {
		boolean isDeleted = reservationService.delete(id);
		if (!isDeleted) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Reservation not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		Map<String, String> response = new HashMap<>();
		response.put("message", "Reservation with ID " + id + " deleted");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Логика обновления информации о бронировании для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateReservationForAdmin(@PathVariable Long id, @RequestBody Reservation reservationData) {
		Reservation existingReservation = reservationService.read(id);
		if (existingReservation == null) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Reservation not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		existingReservation.setCheckinDate(reservationData.getCheckinDate());
		existingReservation.setCheckoutDate(reservationData.getCheckoutDate());
		existingReservation.setNoOfPerson(reservationData.getNoOfPerson());
		existingReservation.setStatus(reservationData.getStatus());
		existingReservation.setRoom(reservationData.getRoom());
		existingReservation.setHotel(reservationData.getHotel());
		existingReservation.setUser(reservationData.getUser());
		reservationService.edit(existingReservation);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Information about reservation with ID " + id + " updated");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
