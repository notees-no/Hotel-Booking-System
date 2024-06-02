package com.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.entity.Hotel;
import com.enums.HotelType;
import com.enums.Role;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

	// Поиск отелей по имени
	List<Hotel> findByName(String name);

	// Поиск отелей по типу
	List<Hotel> findByHotelType(HotelType hotelType);

	// Поиск отелей по роли
	List<Hotel> findByRole(Role role);
}
