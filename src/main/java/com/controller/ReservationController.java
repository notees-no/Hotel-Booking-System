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

	private final ReservationService reservationService;

	@Autowired
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getReservationByIdForUser(@PathVariable Long id) {
		Reservation reservation = reservationService.read(id);
		if (reservation == null) {
			return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(reservation);
	}

	@GetMapping("/status/{status}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getReservationsByStatusForUser(@PathVariable String status) {
		List<Reservation> reservations = reservationService.readByStatus(ReservationStatus.valueOf(status));
		if (reservations.isEmpty()) {
			return new ResponseEntity<>("Reservations not found with status: " + status, HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(reservations);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> addReservationForUser(@RequestBody Reservation reservation) {
		reservationService.save(reservation);
		return new ResponseEntity<>("New reservation added", HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteReservationForAdmin(@PathVariable Long id) {
		reservationService.delete(id);
		return new ResponseEntity<>("Reservation with ID " + id + " deleted", HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateReservationForAdmin(@PathVariable Long id, @RequestBody Reservation reservationData) {
		Reservation existingReservation = reservationService.read(id);
		if (existingReservation == null) {
			return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
		}
		existingReservation.setCheckinDate(reservationData.getCheckinDate());
		existingReservation.setCheckoutDate(reservationData.getCheckoutDate());
		existingReservation.setNoOfPerson(reservationData.getNoOfPerson());
		existingReservation.setStatus(reservationData.getStatus());
		existingReservation.setRoom(reservationData.getRoom());
		existingReservation.setHotel(reservationData.getHotel());
		existingReservation.setUser(reservationData.getUser());
		reservationService.edit(existingReservation);
		return new ResponseEntity<>("Information about reservation with ID " + id + " updated", HttpStatus.OK);
	}
}
