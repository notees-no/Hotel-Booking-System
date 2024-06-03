package com.controller;

import com.entity.Reservation;
import com.entity.User;
import com.enums.Role;
import com.enums.ReservationStatus;
import com.service.ReservationService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	private final ReservationService reservationService;
	private final UserService userService;

	@Autowired
	public ReservationController(ReservationService reservationService, UserService userService) {
		this.reservationService = reservationService;
		this.userService = userService;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<Reservation> getReservationByIdForUser(@PathVariable Long id) {
		Reservation reservation = reservationService.read(id);
		return checkEntityAndRole(reservation);
	}

	@GetMapping("/status/{status}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<List<Reservation>> getReservationsByStatusForUser(@PathVariable String status) {
		List<Reservation> reservations = reservationService.readByStatus(ReservationStatus.valueOf(status));
		return checkListOfEntityAndRole(reservations);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<Reservation> addReservationForUser(@RequestBody Reservation reservation) {
		reservationService.save(reservation);
		return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteReservationForAdmin(@PathVariable Long id) {
		reservationService.delete(id);
		return ResponseEntity.ok("Reservation with ID " + id + " deleted");
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Reservation> updateReservationForAdmin(@PathVariable Long id, @RequestBody Reservation reservationData) {
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
		reservationService.edit(existingReservation);
		return ResponseEntity.ok(existingReservation);
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return userService.getByUsername(currentPrincipalName);
	}

	private ResponseEntity<Reservation> checkEntityAndRole(Reservation reservation) {
		if (reservation == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		User currentUser = getCurrentUser();

		if (currentUser.getRole().equals(Role.ROLE_ADMIN) || currentUser.getRole().equals(Role.ROLE_USER)) {
			return new ResponseEntity<>(reservation, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	private ResponseEntity<List<Reservation>> checkListOfEntityAndRole(List<Reservation> reservations) {
		if (reservations.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Reservation> allowedReservations = new ArrayList<>();
		for (Reservation reservation : reservations) {
			ResponseEntity<Reservation> responseEntity = checkEntityAndRole(reservation);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				allowedReservations.add(reservation);
			}
		}

		if (allowedReservations.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>(allowedReservations, HttpStatus.OK);
	}
}
