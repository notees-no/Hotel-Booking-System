package com.service.impl;

import com.entity.Hotel;
import com.entity.Reservation;
import com.entity.Room;
import com.entity.User;
import com.enums.ReservationStatus;
import com.repository.ReservationRepository;
import com.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Override
	public Reservation read(Long id) {
		return reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
	}

	@Override
	public List<Reservation> read() {
		return reservationRepository.findAll();
	}

	@Override
	public void save(Reservation reservation) {
		reservationRepository.save(reservation);
	}

	@Override
	public void delete(Long id) {
		Reservation reservation = read(id);
		reservationRepository.delete(reservation);
	}

	@Override
	public void edit(Reservation reservation) {
		Reservation existingReservation = read(reservation.getId());
		existingReservation.setCheckinDate(reservation.getCheckinDate());
		existingReservation.setCheckoutDate(reservation.getCheckoutDate());
		existingReservation.setNoOfPerson(reservation.getNoOfPerson());
		existingReservation.setStatus(reservation.getStatus());
		existingReservation.setRoom(reservation.getRoom());
		existingReservation.setHotel(reservation.getHotel());
		existingReservation.setUser(reservation.getUser());
		reservationRepository.save(existingReservation);
	}

	@Override
	public List<Reservation> readByCheckinDate(LocalDate checkinDate) {
		return reservationRepository.findByCheckinDate(checkinDate);
	}

	@Override
	public List<Reservation> readByCheckoutDate(LocalDate checkoutDate) {
		return reservationRepository.findByCheckoutDate(checkoutDate);
	}

	@Override
	public List<Reservation> readByStatus(ReservationStatus status) {
		return reservationRepository.findByStatus(status);
	}

	@Override
	public List<Reservation> readByRoom(Room room) {
		return reservationRepository.findByRoom(room);
	}

	@Override
	public List<Reservation> readByHotel(Hotel hotel) {
		return reservationRepository.findByHotel(hotel);
	}

	@Override
	public List<Reservation> readByUser(User user) {
		return reservationRepository.findByUser(user);
	}
}
