package com.controller;

import com.entity.Hotel;
import com.entity.User;
import com.enums.Role;
import com.service.HotelService;
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
@RequestMapping("/hotel")
public class HotelController {

	private final HotelService hotelService;
	private final UserService userService;

	@Autowired
	public HotelController(HotelService hotelService, UserService userService) {
		this.hotelService = hotelService;
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
		Hotel hotel = hotelService.read(id);
		return checkEntityAndRole(hotel);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Hotel>> getHotelsByName(@PathVariable String name) {
		List<Hotel> hotels = hotelService.readByName(name);
		return checkListOfEntityAndRole(hotels);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
		hotelService.save(hotel);
		return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
		hotelService.delete(id);
		return ResponseEntity.ok("Hotel with ID " + id + " deleted");
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotelData) {
		Hotel existingHotel = hotelService.read(id);
		if (existingHotel == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found");
		}
		existingHotel.setName(hotelData.getName());
		existingHotel.setHotelPhone(hotelData.getHotelPhone());
		existingHotel.setHotelType(hotelData.getHotelType());
		existingHotel.setRole(hotelData.getRole());
		existingHotel.setAmenities(hotelData.getAmenities());
		hotelService.edit(existingHotel);
		return ResponseEntity.ok(existingHotel);
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return userService.getByUsername(currentPrincipalName);
	}

	private ResponseEntity<Hotel> checkEntityAndRole(Hotel hotel) {
		if (hotel == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		User currentUser = getCurrentUser();

		if (currentUser.getRole().equals(Role.ROLE_ADMIN)) {
			return new ResponseEntity<>(hotel, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	private ResponseEntity<List<Hotel>> checkListOfEntityAndRole(List<Hotel> hotels) {
		if (hotels.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Hotel> allowedHotels = new ArrayList<>();
		for (Hotel hotel : hotels) {
			ResponseEntity<Hotel> responseEntity = checkEntityAndRole(hotel);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				allowedHotels.add(hotel);
			}
		}

		if (allowedHotels.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>(allowedHotels, HttpStatus.OK);
	}
}
