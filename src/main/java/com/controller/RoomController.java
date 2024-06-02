package com.controller;

import com.entity.Room;
import com.enums.RoomType;
import com.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@GetMapping("/{id}")
	public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
		Room room = roomService.read(id);
		return room != null ?
				new ResponseEntity<>(room, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/number/{roomNumber}")
	public ResponseEntity<List<Room>> getRoomsByRoomNumber(@PathVariable Integer roomNumber) {
		List<Room> rooms = roomService.readByRoomNumber(roomNumber);
		return !rooms.isEmpty() ?
				new ResponseEntity<>(rooms, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/type/{roomType}")
	public ResponseEntity<List<Room>> getRoomsByRoomType(@PathVariable RoomType roomType) {
		List<Room> rooms = roomService.readByRoomType(roomType);
		return !rooms.isEmpty() ?
				new ResponseEntity<>(rooms, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/person/{noOfPerson}")
	public ResponseEntity<List<Room>> getRoomsByNoOfPerson(@PathVariable Integer noOfPerson) {
		List<Room> rooms = roomService.readByNoOfPerson(noOfPerson);
		return !rooms.isEmpty() ?
				new ResponseEntity<>(rooms, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/price/{price}")
	public ResponseEntity<List<Room>> getRoomsByPrice(@PathVariable BigDecimal price) {
		List<Room> rooms = roomService.readByPrice(price);
		return !rooms.isEmpty() ?
				new ResponseEntity<>(rooms, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/available/{available}")
	public ResponseEntity<List<Room>> getRoomsByAvailable(@PathVariable Boolean available) {
		List<Room> rooms = roomService.readByAvailable(available);
		return !rooms.isEmpty() ?
				new ResponseEntity<>(rooms, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<Void> addRoom(@RequestBody Room room) {
		roomService.save(room);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateRoom(@PathVariable Long id, @RequestBody Room room) {
		room.setId(id);
		roomService.edit(room);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
		roomService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
