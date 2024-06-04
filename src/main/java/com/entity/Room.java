package com.entity;

import java.math.BigDecimal;
import java.util.List;

import com.enums.RoomType;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name= "rooms")
@AttributeOverride(name = "id", column = @Column(name = "room_id"))
public class Room extends AbstractEntity {

	private Integer roomNumber;

	@Enumerated(EnumType.STRING)
	private RoomType roomType;

	private Integer noOfPerson;

	private BigDecimal price;

	private Boolean available;

	@ManyToOne(fetch = FetchType.EAGER)
	private Hotel hotel;

	@OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
	private List<Reservation> reservations;

}
