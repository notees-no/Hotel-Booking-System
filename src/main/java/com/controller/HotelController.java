package com.controller;

import com.entity.Hotel;
import com.enums.HotelType;
import com.enums.Role;
import com.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@GetMapping("/{id}")
	public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
		Hotel hotel = hotelService.read(id);
		return hotel != null ?
				new ResponseEntity<>(hotel, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Hotel>> getHotelByName(@PathVariable String name) {
		List<Hotel> hotelList = hotelService.readByName(name);
		return hotelList != null && !hotelList.isEmpty() ?
				new ResponseEntity<>(hotelList, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/type/{type}")
	public ResponseEntity<List<Hotel>> getHotelByType(@PathVariable HotelType type) {
		List<Hotel> hotelList = hotelService.readByHotelType(type);
		return hotelList != null && !hotelList.isEmpty() ?
				new ResponseEntity<>(hotelList, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/role/{role}")
	public ResponseEntity<List<Hotel>> getHotelByRole(@PathVariable Role role) {
		List<Hotel> hotelList = hotelService.readByRole(role);
		return hotelList != null && !hotelList.isEmpty() ?
				new ResponseEntity<>(hotelList, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<Void> addHotel(@RequestBody Hotel hotel) {
		hotelService.save(hotel);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
		hotel.setId(id);
		hotelService.edit(hotel);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
		hotelService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
