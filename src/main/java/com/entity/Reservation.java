package com.entity;

import java.time.LocalDate;

import com.enums.ReservationStatus;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name= "reservations")
@AttributeOverride(name = "id", column = @Column(name = "reservation_id"))
public class Reservation extends AbstractEntity {

	private LocalDate checkinDate;

	private LocalDate checkoutDate;

	private Integer noOfPerson;

	@Embedded

	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	@ManyToOne(fetch = FetchType.EAGER)
	private Room room;

	@ManyToOne(fetch = FetchType.EAGER)
	private Hotel hotel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
}
