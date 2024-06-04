package com.controller;

import com.entity.Hotel;
import com.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {

	private final HotelService hotelService;

	@Autowired
	public HotelController(HotelService hotelService) {
		this.hotelService = hotelService;
	}

	// Логика получения информации об отеле для неавторизованных
	@GetMapping("/{id}")
	public ResponseEntity<?> getHotelByIdForUnauthorized(@PathVariable Long id) {
		Hotel hotel = hotelService.read(id);
		if (hotel == null) {
			return new ResponseEntity<>("Hotel not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(hotel, HttpStatus.OK);
	}

	// Логика получения списка отелей по названию для неавторизованных
	@GetMapping("/name/{name}")
	public ResponseEntity<?> getHotelsByNameForUnauthorized(@PathVariable String name) {
		List<Hotel> hotels = hotelService.readByName(name);
		if (hotels.isEmpty()) {
			return new ResponseEntity<>("Hotels not found with name: " + name, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(hotels, HttpStatus.OK);
	}

	// Логика добавления нового отеля для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addHotelForAdmin(@RequestBody Hotel hotel) {
		hotelService.save(hotel);
		return new ResponseEntity<>("New hotel added", HttpStatus.CREATED);
	}

	// Логика удаления отеля для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteHotelForAdmin(@PathVariable Long id) {
		boolean isDeleted = hotelService.delete(id);
		if (!isDeleted) {
			return new ResponseEntity<>("Hotel not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("Hotel with ID " + id + " deleted", HttpStatus.OK);
	}

	// Логика обновления информации об отеле для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateHotelForAdmin(@PathVariable Long id, @RequestBody Hotel hotelData) {
		Hotel existingHotel = hotelService.read(id);
		if (existingHotel == null) {
			return new ResponseEntity<>("Hotel not found", HttpStatus.NOT_FOUND);
		}
		existingHotel.setName(hotelData.getName());
		existingHotel.setHotelPhone(hotelData.getHotelPhone());
		existingHotel.setHotelType(hotelData.getHotelType());
		existingHotel.setRole(hotelData.getRole());
		existingHotel.setAmenities(hotelData.getAmenities());
		hotelService.edit(existingHotel);
		return new ResponseEntity<>("Information about hotel with ID " + id + " updated", HttpStatus.OK);
	}
}
