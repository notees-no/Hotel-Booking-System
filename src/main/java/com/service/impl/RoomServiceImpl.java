package com.service.impl;

import com.entity.Hotel;
import com.entity.Room;
import com.enums.RoomType;
import com.repository.RoomRepository;
import com.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public Room read(Long id) {
		return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found"));
	}

	@Override
	public List<Room> read() {
		return roomRepository.findAll();
	}

	@Override
	public void save(Room room) {
		roomRepository.save(room);
	}

	@Override
	public boolean delete(Long id) {
		Room room = read(id);
		roomRepository.delete(room);
        return false;
    }

	@Override
	public void edit(Room room) {
		Room existingRoom = read(room.getId());
		existingRoom.setRoomNumber(room.getRoomNumber());
		existingRoom.setRoomType(room.getRoomType());
		existingRoom.setNoOfPerson(room.getNoOfPerson());
		existingRoom.setPrice(room.getPrice());
		existingRoom.setAvailable(room.getAvailable());
		existingRoom.setHotel(room.getHotel());
		roomRepository.save(existingRoom);
	}

	@Override
	public List<Room> readByRoomNumber(Integer roomNumber) {
		return roomRepository.findByRoomNumber(roomNumber);
	}

	@Override
	public List<Room> readByRoomType(RoomType roomType) {
		return roomRepository.findByRoomType(roomType);
	}

	@Override
	public List<Room> readByNoOfPerson(Integer noOfPerson) {
		return roomRepository.findByNoOfPerson(noOfPerson);
	}

	@Override
	public List<Room> readByPrice(BigDecimal price) {
		return roomRepository.findByPrice(price);
	}

	@Override
	public List<Room> readByAvailable(Boolean available) {
		return roomRepository.findByAvailable(available);
	}

	@Override
	public List<Room> readByHotel(Hotel hotel) {
		return roomRepository.findByHotel(hotel);
	}
}
