package com.entity;

import java.util.List;
import com.enums.HotelType;
import com.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "hotels")
@AttributeOverride(name = "id", column = @Column(name = "hotel_id"))
public class Hotel extends AbstractEntity {

	private String name;

	@Column(unique = true)

	private String hotelPhone;

	@Enumerated(EnumType.STRING)
	private HotelType hotelType;

	@Enumerated(EnumType.STRING)
	private Role role;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> amenities;

	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
	private List<Room> rooms;

	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
	private List<Reservation> reservations;

}
