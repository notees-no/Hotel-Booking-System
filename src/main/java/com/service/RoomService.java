package com.service;

import com.entity.Room;
import com.entity.Hotel;
import com.enums.RoomType;
import java.math.BigDecimal;
import java.util.List;

public interface RoomService extends Service<Room> {

	List<Room> readByRoomNumber(Integer roomNumber);

	List<Room> readByRoomType(RoomType roomType);

	List<Room> readByNoOfPerson(Integer noOfPerson);

	List<Room> readByPrice(BigDecimal price);

	List<Room> readByAvailable(Boolean available);

	List<Room> readByHotel(Hotel hotel);
}
