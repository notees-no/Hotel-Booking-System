DROP DATABASE IF EXISTS staywell;
CREATE DATABASE staywell;
USE staywell;

-- Удаление таблиц, если они существуют
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS hotels;

-- Создание таблицы hotels
CREATE TABLE IF NOT EXISTS `hotels` (
    `hotel_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NULL,
    `hotelPhone` VARCHAR(255) NULL,
    `hotelType` ENUM('HOTEL') NULL,
    `role` ENUM('ROLE_ADMIN','ROLE_USER') NULL,
    PRIMARY KEY (`hotel_id`)
);

-- Создание таблицы users
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(100) NOT NULL,
    `password` VARCHAR(255) NOT NULL, -- Используем VARCHAR(255) для хранения хеша пароля
    `role` ENUM('ROLE_ADMIN','ROLE_USER') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_UNIQUE` (`username`) -- Добавляем уникальный индекс для имени пользователя
);

-- Создание таблицы rooms
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

-- Создание таблицы reservations
CREATE TABLE IF NOT EXISTS `reservations` (
    `reservation_id` BIGINT NOT NULL AUTO_INCREMENT,
    `checkinDate` DATE NULL,
    `checkoutDate` DATE NULL,
    `noOfPerson` INT NULL,
    `status` ENUM('BOOKED','CLOSED') NULL,
    `room_id` BIGINT NULL, -- Поле для хранения идентификатора комнаты
    `hotel_id` BIGINT NULL, -- Поле для хранения идентификатора отеля
    `user_id` BIGINT NULL, -- Поле для хранения идентификатора пользователя
    PRIMARY KEY (`reservation_id`),
    INDEX `FK_reservations_room_idx` (`room_id` ASC),
    INDEX `FK_reservations_hotel_idx` (`hotel_id` ASC),
    INDEX `FK_reservations_user_idx` (`user_id` ASC),
    CONSTRAINT `FK_reservations_room`
        FOREIGN KEY (`room_id`)
        REFERENCES `rooms` (`room_id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `FK_reservations_hotel`
        FOREIGN KEY (`hotel_id`)
        REFERENCES `hotels` (`hotel_id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `FK_reservations_user`
        FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- Заполнение таблицы hotels
INSERT INTO hotels (hotel_id, name, hotelPhone, hotelType, role)
VALUES (1, 'Hotel A', '123456789', 'HOTEL', 'ROLE_ADMIN'),
       (2, 'Hotel B', '987654321', 'HOTEL', 'ROLE_USER');

-- Заполнение таблиц users
INSERT INTO users (id, username, password, role)
VALUES (1, 'admin', '$2a$10$4ntneHfvOYZFg8JPeHxh1.S89KlFsmRN/Gu0VWIZ/8ZeqIVUIBMLC', 'ROLE_ADMIN'),
       (2, 'user1', '$2a$10$BnXIcgFcXjCn0oC4atAVP.bIAk.HUE4y/oK.BW/awcGO8.trcX5XK', 'ROLE_USER');

-- Заполнение таблиц rooms
INSERT INTO rooms (room_id, roomNumber, roomType, noOfPerson, price, available, hotel_hotel_id)
VALUES (1, 101, 'AC', 2, 100.00, true, 1),
       (2, 102, 'NON_AC', 2, 80.00, true, 1),
       (3, 201, 'AC', 3, 150.00, true, 2),
       (4, 202, 'AC', 3, 150.00, true, 2);

-- Заполнение таблиц reservations
INSERT INTO reservations (reservation_id, checkinDate, checkoutDate, noOfPerson, status, room_id, hotel_id, user_id)
VALUES (1, '2024-06-10', '2024-06-15', 2, 'BOOKED', 1, 1, 1),
       (2, '2024-06-20', '2024-06-25', 3, 'BOOKED', 3, 2, 2);
