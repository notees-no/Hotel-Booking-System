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

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	// Для всех пользователей
	@GetMapping("/{id}")
	public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
		Reservation reservation = reservationService.read(id);
		return reservation != null ?
				new ResponseEntity<>(reservation, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// Для всех пользователей
	@GetMapping("/status/{status}")
	public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable ReservationStatus status) {
		List<Reservation> reservations = reservationService.readByStatus(status);
		return !reservations.isEmpty() ?
				new ResponseEntity<>(reservations, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// Доступ только для администратора
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Void> addReservation(@RequestBody Reservation reservation) {
		reservationService.save(reservation);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// Доступ только для администратора
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
		reservationService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Доступ только для администратора
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
		reservation.setId(id);
		reservationService.edit(reservation);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
