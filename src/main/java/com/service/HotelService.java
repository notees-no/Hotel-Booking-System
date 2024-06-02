package com.service;

import com.entity.Hotel;
import com.enums.HotelType;
import com.enums.Role;
import java.util.List;

public interface HotelService extends Service<Hotel> {

	List<Hotel> readByName(String name);

	List<Hotel> readByHotelType(HotelType hotelType);

	List<Hotel> readByRole(Role role);
}
