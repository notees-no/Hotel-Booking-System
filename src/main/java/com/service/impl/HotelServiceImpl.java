package com.service.impl;

import com.entity.Hotel;
import com.enums.HotelType;
import com.enums.Role;
import com.repository.HotelRepository;
import com.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

	@Autowired
	private HotelRepository hotelRepository;

	@Override
	public Hotel read(Long id) {
		return hotelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
	}

	@Override
	public List<Hotel> read() {
		return hotelRepository.findAll();
	}

	@Override
	public void save(Hotel hotel) {
		hotelRepository.save(hotel);
	}

	@Override
	public void delete(Long id) {
		Hotel hotel = read(id);
		hotelRepository.delete(hotel);
	}

	@Override
	public void edit(Hotel hotel) {
		Hotel existingHotel = read(hotel.getId());
		existingHotel.setName(hotel.getName());
		existingHotel.setHotelEmail(hotel.getHotelEmail());
		existingHotel.setPassword(hotel.getPassword());
		existingHotel.setHotelPhone(hotel.getHotelPhone());
		existingHotel.setHotelTelephone(hotel.getHotelTelephone());
		existingHotel.setHotelType(hotel.getHotelType());
		existingHotel.setRole(hotel.getRole());
		existingHotel.setAmenities(hotel.getAmenities());
		hotelRepository.save(existingHotel);
	}

	@Override
	public List<Hotel> readByName(String name) {
		return hotelRepository.findByName(name);
	}

	@Override
	public List<Hotel> readByHotelType(HotelType hotelType) {
		return hotelRepository.findByHotelType(hotelType);
	}

	@Override
	public List<Hotel> readByRole(Role role) {
		return hotelRepository.findByRole(role);
	}
}
