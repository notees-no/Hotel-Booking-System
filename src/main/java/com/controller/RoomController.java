package com.controller;

import com.entity.Room;
import com.entity.User;
import com.enums.Role;
import com.service.RoomService;
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
@RequestMapping("/room")
public class RoomController {

	private final RoomService roomService;
	private final UserService userService;

	@Autowired
	public RoomController(RoomService roomService, UserService userService) {
		this.roomService = roomService;
		this.userService = userService;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<Room> getRoomByIdForUser(@PathVariable Long id) {
		Room room = roomService.read(id);
		return checkEntityAndRole(room);
	}

	@GetMapping("/number/{roomNumber}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<List<Room>> getRoomsByNumberForUser(@PathVariable Integer roomNumber) {
		List<Room> rooms = roomService.readByRoomNumber(roomNumber);
		return checkListOfEntityAndRole(rooms);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Room> addRoomForAdmin(@RequestBody Room room) {
		roomService.save(room);
		return ResponseEntity.status(HttpStatus.CREATED).body(room);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteRoomForAdmin(@PathVariable Long id) {
		roomService.delete(id);
		return ResponseEntity.ok("Room with ID " + id + " deleted");
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Room> updateRoomForAdmin(@PathVariable Long id, @RequestBody Room roomData) {
		Room existingRoom = roomService.read(id);
		if (existingRoom == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
		}
		existingRoom.setRoomNumber(roomData.getRoomNumber());
		existingRoom.setPrice(roomData.getPrice());
		roomService.edit(existingRoom);
		return ResponseEntity.ok(existingRoom);
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return userService.getByUsername(currentPrincipalName);
	}

	private ResponseEntity<Room> checkEntityAndRole(Room room) {
		if (room == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		User currentUser = getCurrentUser();

		if (currentUser.getRole().equals(Role.ROLE_ADMIN) || currentUser.getRole().equals(Role.ROLE_USER)) {
			return new ResponseEntity<>(room, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	private ResponseEntity<List<Room>> checkListOfEntityAndRole(List<Room> rooms) {
		if (rooms.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Room> allowedRooms = new ArrayList<>();
		for (Room room : rooms) {
			ResponseEntity<Room> responseEntity = checkEntityAndRole(room);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				allowedRooms.add(room);
			}
		}

		if (allowedRooms.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>(allowedRooms, HttpStatus.OK);
	}
}
