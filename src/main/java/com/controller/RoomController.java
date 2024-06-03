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

	private final RoomService roomService;

	@Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getRoomByIdForUser(@PathVariable Long id) {
		Room room = roomService.read(id);
		if (room == null) {
			return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(room);
	}

	@GetMapping("/number/{roomNumber}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getRoomsByNumberForUser(@PathVariable Integer roomNumber) {
		List<Room> rooms = roomService.readByRoomNumber(roomNumber);
		if (rooms.isEmpty()) {
			return new ResponseEntity<>("Rooms not found with number: " + roomNumber, HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(rooms);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addRoomForAdmin(@RequestBody Room room) {
		roomService.save(room);
		return new ResponseEntity<>("New room added", HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteRoomForAdmin(@PathVariable Long id) {
		roomService.delete(id);
		return new ResponseEntity<>("Room with ID " + id + " deleted", HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateRoomForAdmin(@PathVariable Long id, @RequestBody Room roomData) {
		Room existingRoom = roomService.read(id);
		if (existingRoom == null) {
			return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
		}
		existingRoom.setRoomNumber(roomData.getRoomNumber());
		existingRoom.setRoomType(roomData.getRoomType());
		existingRoom.setNoOfPerson(roomData.getNoOfPerson());
		existingRoom.setPrice(roomData.getPrice());
		existingRoom.setAvailable(roomData.getAvailable());
		existingRoom.setHotel(roomData.getHotel());
		roomService.edit(existingRoom);
		return new ResponseEntity<>("Information about room with ID " + id + " updated", HttpStatus.OK);
	}
}
