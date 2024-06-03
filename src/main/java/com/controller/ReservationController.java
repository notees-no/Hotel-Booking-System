package com.controller;

import com.entity.Reservation;
import com.enums.ReservationStatus;
import com.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
	public ResponseEntity<Reservation> getReservationByIdForUser(@PathVariable Long id) {
		Reservation reservation = reservationService.read(id);
		if (reservation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
		}
		return ResponseEntity.ok(reservation);
	}

	// Логика получения списка бронирований по статусу для пользователей
	@GetMapping("/status/{status}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<List<Reservation>> getReservationsByStatusForUser(@PathVariable String status) {
		List<Reservation> reservations = reservationService.readByStatus(ReservationStatus.valueOf(status));
		if (reservations.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservations not found with status: " + status);
		}
		return ResponseEntity.ok(reservations);
	}

	// Логика добавления нового бронирования для пользователей
	@PostMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<String> addReservationForUser(@RequestBody Reservation reservation) {
		reservationService.save(reservation);
		return ResponseEntity.ok("New reservation added");
	}

	// Логика удаления бронирования для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteReservationForAdmin(@PathVariable Long id) {
		reservationService.delete(id);
		return ResponseEntity.ok("Reservation with ID " + id + " deleted");
	}

	// Логика обновления информации о бронировании для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateReservationForAdmin(@PathVariable Long id, @RequestBody Reservation reservationData) {
		Reservation existingReservation = reservationService.read(id);
		if (existingReservation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
		}
		existingReservation.setCheckinDate(reservationData.getCheckinDate());
		existingReservation.setCheckoutDate(reservationData.getCheckoutDate());
		existingReservation.setNoOfPerson(reservationData.getNoOfPerson());
		existingReservation.setStatus(reservationData.getStatus());
		existingReservation.setRoom(reservationData.getRoom());
		existingReservation.setHotel(reservationData.getHotel());
		existingReservation.setUser(reservationData.getUser());
		reservationService.edit(existingReservation);
		return ResponseEntity.ok("Information about reservation with ID " + id + " updated");
	}
}
