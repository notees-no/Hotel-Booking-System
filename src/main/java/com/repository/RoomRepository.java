package com.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.entity.Room;
import com.entity.Hotel;
import com.enums.RoomType;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	// Поиск комнат по номеру комнаты
	List<Room> findByRoomNumber(Integer roomNumber);

	// Поиск комнат по типу комнаты
	List<Room> findByRoomType(RoomType roomType);

	// Поиск комнат по количеству человек
	List<Room> findByNoOfPerson(Integer noOfPerson);

	// Поиск комнат по цене
	List<Room> findByPrice(BigDecimal price);

	// Поиск доступных комнат
	List<Room> findByAvailable(Boolean available);

	// Поиск комнат по отелю
	List<Room> findByHotel(Hotel hotel);
}
