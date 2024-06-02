package com.controller;

import com.entity.Room;
import com.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private RoomService roomService;

	// Для всех пользователей
	@GetMapping("/{id}")
	public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
		Room room = roomService.read(id);
		return room != null ?
				new ResponseEntity<>(room, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// Для всех пользователей
	@GetMapping("/number/{roomNumber}")
	public ResponseEntity<List<Room>> getRoomsByRoomNumber(@PathVariable Integer roomNumber) {
		List<Room> rooms = roomService.readByRoomNumber(roomNumber);
		return !rooms.isEmpty() ?
				new ResponseEntity<>(rooms, HttpStatus.OK) :
				new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// Доступ только для администратора
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Void> addRoom(@RequestBody Room room) {
		roomService.save(room);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// Доступ только для администратора
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
		roomService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Доступ только для администратора
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateRoom(@PathVariable Long id, @RequestBody Room room) {
		room.setId(id);
		roomService.edit(room);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
