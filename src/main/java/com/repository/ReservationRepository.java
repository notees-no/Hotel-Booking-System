package com.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.entity.Reservation;
import com.entity.Hotel;
import com.entity.Room;
import com.entity.User;
import com.enums.ReservationStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	// Поиск бронирований по дате заезда
	List<Reservation> findByCheckinDate(LocalDate checkinDate);

	// Поиск бронирований по дате выезда
	List<Reservation> findByCheckoutDate(LocalDate checkoutDate);

	// Поиск бронирований по статусу
	List<Reservation> findByStatus(ReservationStatus status);

	// Поиск бронирований по номеру комнаты
	List<Reservation> findByRoom(Room room);

	// Поиск бронирований по отелю
	List<Reservation> findByHotel(Hotel hotel);

	// Поиск бронирований по пользователю
	List<Reservation> findByUser(User user);
}
