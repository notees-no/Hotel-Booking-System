package com.controller;

import com.entity.Hotel;
import com.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
	public ResponseEntity<Hotel> getHotelByIdForUnauthorized(@PathVariable Long id) {
		Hotel hotel = hotelService.read(id);
		if (hotel == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found");
		}
		return ResponseEntity.ok(hotel);
	}

	// Логика получения списка отелей по названию для неавторизованных
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Hotel>> getHotelsByNameForUnauthorized(@PathVariable String name) {
		List<Hotel> hotels = hotelService.readByName(name);
		if (hotels.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotels not found with name: " + name);
		}
		return ResponseEntity.ok(hotels);
	}

	// Логика добавления нового отеля для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addHotelForAdmin(@RequestBody Hotel hotel) {
		hotelService.save(hotel);
		return ResponseEntity.ok("New hotel added");
	}

	// Логика удаления отеля для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteHotelForAdmin(@PathVariable Long id) {
		hotelService.delete(id);
		return ResponseEntity.ok("Hotel with ID " + id + " deleted");
	}

	// Логика обновления информации об отеле для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateHotelForAdmin(@PathVariable Long id, @RequestBody Hotel hotelData) {
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
		return ResponseEntity.ok("Information about hotel with ID " + id + " updated");
	}
}
