DROP DATABASE IF EXISTS staywell;
CREATE DATABASE staywell;
USE staywell;

CREATE DATABASE IF NOT EXISTS hotel_booking DEFAULT CHARACTER SET utf8;
USE hotel_booking;

-- Удаление внешних ключей перед удалением таблиц
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS users;

-- Создание таблиц
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(100) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `role` ENUM('ROLE_ADMIN','ROLE_USER') NOT NULL,
    `authority` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `rooms` (
    `room_id` BIGINT NOT NULL AUTO_INCREMENT,
    `roomNumber` INT NULL,
    `roomType` ENUM('AC','NON_AC') NULL,
    `noOfPerson` INT NULL,
    `price` DECIMAL(10,2) NULL,
    `available` BOOLEAN NULL,
    `hotel_hotel_id` BIGINT NULL,
    PRIMARY KEY (`room_id`),
    INDEX `FK_rooms_hotel_idx` (`hotel_hotel_id` ASC),
    CONSTRAINT `FK_rooms_hotel`
        FOREIGN KEY (`hotel_hotel_id`)
        REFERENCES `hotels` (`hotel_id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `reservations` (
    `reservation_id` BIGINT NOT NULL AUTO_INCREMENT,
    `checkinDate` DATE NULL,
    `checkoutDate` DATE NULL,
    `noOfPerson` INT NULL,
    `status` ENUM('BOOKED','CLOSED') NULL,
    `room_room_id` BIGINT NULL,
    `hotel_hotel_id` BIGINT NULL,
    `user_id` BIGINT NULL,
    PRIMARY KEY (`reservation_id`),
    INDEX `FK_reservations_room_idx` (`room_room_id` ASC),
    INDEX `FK_reservations_hotel_idx` (`hotel_hotel_id` ASC),
    INDEX `FK_reservations_user_idx` (`user_id` ASC),
    CONSTRAINT `FK_reservations_room`
        FOREIGN KEY (`room_room_id`)
        REFERENCES `rooms` (`room_id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `FK_reservations_hotel`
        FOREIGN KEY (`hotel_hotel_id`)
        REFERENCES `hotels` (`hotel_id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `FK_reservations_user`
        FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `hotels` (
    `hotel_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NULL,
    `hotelPhone` VARCHAR(255) NULL,
    `hotelTelephone` VARCHAR(255) NULL,
    `hotelType` ENUM('HOTEL') NULL,
    `role` ENUM('ROLE_ADMIN','ROLE_USER') NULL,
    PRIMARY KEY (`hotel_id`)
);

-- Заполнение таблицы hotels
INSERT INTO hotels (name, hotelPhone, hotelType, role)
VALUES ('Hotel A', '123456789', 'HOTEL', 'ROLE_ADMIN'),
       ('Hotel B', '987654321', 'HOTEL', 'ROLE_USER');

-- Заполнение таблицы rooms
INSERT INTO rooms (roomNumber, roomType, noOfPerson, price, available, hotel_hotel_id)
VALUES (101, 'AC', 2, 100.00, true, 1),
       (102, 'NON_AC', 2, 80.00, true, 1),
       (201, 'AC', 3, 150.00, true, 2),
       (202, 'AC', 3, 150.00, true, 2);

-- Заполнение таблицы users
INSERT INTO users (username, password, role, authority)
VALUES ('admin', '$2a$12$/qIUg5u9m1JS.iF7cVOYfe8Yfp0FMY9.9CIoO/Hug30u4e86L940O', 'ROLE_ADMIN', 'ROLE_ADMIN'),
       ('user1', '$2a$12$MekFjAC2oOQgDiiFFFUH8.wGRvV.f7lDjp1lqMvGJFU7ZoZUl.V8S', 'ROLE_USER', 'ROLE_USER');

-- Заполнение таблицы reservations
INSERT INTO reservations (checkinDate, checkoutDate, noOfPerson, status, room_room_id, hotel_hotel_id, user_id)
VALUES ('2024-06-10', '2024-06-15', 2, 'BOOKED', 1, 1, 1),
       ('2024-06-20', '2024-06-25', 3, 'BOOKED', 3, 2, 2);
