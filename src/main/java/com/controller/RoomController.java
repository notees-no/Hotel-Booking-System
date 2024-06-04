package com.controller;

import com.entity.Room;
import com.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

	private final RoomService roomService;

	@Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	// Логика получения информации о номере для пользователей
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<Room> getRoomByIdForUser(@PathVariable Long id) {
		Room room = roomService.read(id);
		if (room == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
		}
		return ResponseEntity.ok(room);
	}

	// Логика получения списка номеров по номеру для пользователей
	@GetMapping("/number/{roomNumber}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<List<Room>> getRoomsByNumberForUser(@PathVariable Integer roomNumber) {
		List<Room> rooms = roomService.readByRoomNumber(roomNumber);
		if (rooms.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rooms not found with number: " + roomNumber);
		}
		return ResponseEntity.ok(rooms);
	}

	// Логика добавления нового номера для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addRoomForAdmin(@RequestBody Room room) {
		roomService.save(room);
		return ResponseEntity.ok("New room added");
	}

	// Логика удаления номера для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteRoomForAdmin(@PathVariable Long id) {
		roomService.delete(id);
		return ResponseEntity.ok("Room with ID " + id + " deleted");
	}

	// Логика обновления информации о номере для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateRoomForAdmin(@PathVariable Long id, @RequestBody Room roomData) {
		Room existingRoom = roomService.read(id);
		if (existingRoom == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
		}
		existingRoom.setRoomNumber(roomData.getRoomNumber());
		existingRoom.setRoomType(roomData.getRoomType());
		existingRoom.setNoOfPerson(roomData.getNoOfPerson());
		existingRoom.setPrice(roomData.getPrice());
		existingRoom.setAvailable(roomData.getAvailable());
		existingRoom.setHotel(roomData.getHotel());
		roomService.edit(existingRoom);
		return ResponseEntity.ok("Information about room with ID " + id + " updated");
	}
}
