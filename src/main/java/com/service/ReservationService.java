package com.service;

import com.entity.Reservation;
import com.entity.Hotel;
import com.entity.Room;
import com.entity.User;
import com.enums.ReservationStatus;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService extends Service<Reservation> {

	List<Reservation> readByCheckinDate(LocalDate checkinDate);

	List<Reservation> readByCheckoutDate(LocalDate checkoutDate);

	List<Reservation> readByStatus(ReservationStatus status);

	List<Reservation> readByRoom(Room room);

	List<Reservation> readByHotel(Hotel hotel);

	List<Reservation> readByUser(User user);
}
