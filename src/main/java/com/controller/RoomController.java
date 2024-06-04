package com.controller;

import com.entity.Room;
import com.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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
	public ResponseEntity<?> getRoomByIdForUser(@PathVariable Long id) {
		Room room = roomService.read(id);
		if (room == null) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Room not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(room, HttpStatus.OK);
	}

	// Логика получения списка номеров по номеру для пользователей
	@GetMapping("/number/{roomNumber}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getRoomsByNumberForUser(@PathVariable Integer roomNumber) {
		List<Room> rooms = roomService.readByRoomNumber(roomNumber);
		if (rooms.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Rooms not found with number: " + roomNumber);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(rooms, HttpStatus.OK);
	}

	// Логика добавления нового номера для администраторов
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addRoomForAdmin(@RequestBody Room room) {
		roomService.save(room);
		Map<String, String> response = new HashMap<>();
		response.put("message", "New room added");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Логика удаления номера для администраторов
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteRoomForAdmin(@PathVariable Long id) {
		boolean isDeleted = roomService.delete(id);
		if (!isDeleted) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Room not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		Map<String, String> response = new HashMap<>();
		response.put("message", "Room with ID " + id + " deleted");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Логика обновления информации о номере для администраторов
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateRoomForAdmin(@PathVariable Long id, @RequestBody Room roomData) {
		Room existingRoom = roomService.read(id);
		if (existingRoom == null) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Room not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		existingRoom.setRoomNumber(roomData.getRoomNumber());
		existingRoom.setRoomType(roomData.getRoomType());
		existingRoom.setNoOfPerson(roomData.getNoOfPerson());
		existingRoom.setPrice(roomData.getPrice());
		existingRoom.setAvailable(roomData.getAvailable());
		existingRoom.setHotel(roomData.getHotel());
		roomService.edit(existingRoom);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Information about room with ID " + id + " updated");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
