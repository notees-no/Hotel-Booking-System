package com.controller;

import com.entity.Hotel;
import com.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

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
			Map<String, String> response = new HashMap<>();
			response.put("error", "Hotel not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(hotel, HttpStatus.OK);
	}

	// Логика получения списка отелей по названию для неавторизованных
	@GetMapping("/name/{name}")
	public ResponseEntity<?> getHotelsByNameForUnauthorized(@PathVariable String name) {
		List<Hotel> hotels = hotelService.readByName(name);
		if (hotels.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Hotels not found with name: " + name);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(hotels, HttpStatus.OK);
	}

	// Логика добавления нового отеля для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addHotelForAdmin(@RequestBody Hotel hotel) {
		hotelService.save(hotel);
		Map<String, String> response = new HashMap<>();
		response.put("message", "New hotel added");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Логика удаления отеля для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteHotelForAdmin(@PathVariable Long id) {
		boolean isDeleted = hotelService.delete(id);
		if (!isDeleted) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Hotel not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		Map<String, String> response = new HashMap<>();
		response.put("message", "Hotel with ID " + id + " deleted");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Логика обновления информации об отеле для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateHotelForAdmin(@PathVariable Long id, @RequestBody Hotel hotelData) {
		Hotel existingHotel = hotelService.read(id);
		if (existingHotel == null) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Hotel not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		existingHotel.setName(hotelData.getName());
		existingHotel.setHotelPhone(hotelData.getHotelPhone());
		existingHotel.setHotelType(hotelData.getHotelType());
		existingHotel.setRole(hotelData.getRole());
		existingHotel.setAmenities(hotelData.getAmenities());
		hotelService.edit(existingHotel);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Information about hotel with ID " + id + " updated");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
