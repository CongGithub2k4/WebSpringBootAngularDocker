-- Kết nối đến PDB2 và đặt schema
ALTER SESSION SET CONTAINER = PDB2;
ALTER SESSION SET CURRENT_SCHEMA = PDB2_ADMIN;

-- Insert 30 regular users
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Văn An','user1@gmail.com', '01234567891', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Văn Bê','user2@gmail.com', '01234567892', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Văn Cê','user3@gmail.com', '01234567893', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Văn Đê','user4@gmail.com', '01234567894', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Văn E','user5@gmail.com', '01234567895', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Thị Lan','user6@gmail.com', '01234567896', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Thị Hoa','user7@gmail.com', '01234567897', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Văn Hoàng','user8@gmail.com', '01234567898', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Thị Hà','user9@gmail.com', '01234567899', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Thị Lan','user10@gmail.com', '012345678910', '123456');
INSERT INTO UserTable (user_name,email, phone_number, user_password) VALUES('Nguyễn Thị Mộc','user14@gmail.com', '012345678914', '123456');


-- Insert 3 admin users
INSERT INTO UserTable (user_name,email, phone_number, user_password, is_admin) VALUES('Nguyễn Thị Admin1','admin1@gmail.com', '012345678911', '123456', 1);
INSERT INTO UserTable (user_name,email, phone_number, user_password, is_admin) VALUES('Nguyễn Thị Admin2','admin2@gmail.com', '012345678912', '123456', 1);
INSERT INTO UserTable (user_name,email, phone_number, user_password, is_admin) VALUES('Nguyễn Thị Admin3','admin3@gmail.com', '012345678913', '123456', 1);

-- Create admin records (assuming user_ids 11-13 are the admins)
INSERT INTO AdminTable (admin_id, user_id) VALUES(1, 11);
INSERT INTO AdminTable (admin_id, user_id) VALUES(2, 12);
INSERT INTO AdminTable (admin_id, user_id) VALUES(3, 13);

COMMIT;
-- Insert 14 tours

-- Add Tour and Add destinations for each tour (3-5 per tour)
-- Tour 1 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hà Nội', 'Vịnh Hạ Long', 3, 2, 'assets/Image/Tour/Tour1.jpg', 1);
INSERT INTO TourDestination VALUES (1, 'Hạ Long');
INSERT INTO TourDestination VALUES (1, 'Hà Nội');

-- Tour 2 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hồ Chí Minh', 'Du lịch thành phố Hồ Chí Minh - Sông Cử Long', 2, 1, 'assets/Image/Tour/Tour2.jpg', 2);
INSERT INTO TourDestination VALUES (2, 'Hồ Chí Minh');
INSERT INTO TourDestination VALUES (2, 'Cần Thơ');
INSERT INTO TourDestination VALUES (2, 'Cà Mau');

-- Tour 3 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Đà Nẵng', 'Đà Nẵng - Hội An - Huế', 2, 1, 'assets/Image/Tour/Tour3.jpg', 2);
INSERT INTO TourDestination VALUES (3, 'Hội An');
INSERT INTO TourDestination VALUES (3, 'Đà Nẵng');
INSERT INTO TourDestination VALUES (3, 'Huế');

-- Tour 4 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hà Nội', 'Sapa - Lào Cai - Điện Biên - Hà Giang', 4, 3, 'assets/Image/Tour/Tour4.jpg', 3);
INSERT INTO TourDestination VALUES (4, 'Sapa');
INSERT INTO TourDestination VALUES (4, 'Điện Biên');
INSERT INTO TourDestination VALUES (4, 'Hà Giang');
INSERT INTO TourDestination VALUES (4, 'Lào Cai');

-- Tour 5 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Đà Nẵng', 'Xuyên Việt', 10, 9, 'assets/Image/Tour/Tour5.jpg', 2);
INSERT INTO TourDestination VALUES (5, 'Đà Nẵng');
INSERT INTO TourDestination VALUES (5, 'Hội An');
INSERT INTO TourDestination VALUES (5, 'Hạ Long');
INSERT INTO TourDestination VALUES (5, 'Sapa');
INSERT INTO TourDestination VALUES (5, 'Hà Nội');
INSERT INTO TourDestination VALUES (5, 'Cà Mau');
INSERT INTO TourDestination VALUES (5, 'Phú Quốc');

-- Tour 6 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hà Nội', 'Phố cổ Hội An', 10, 9, 'assets/Image/Tour/Tour6.jpg', 2);
INSERT INTO TourDestination VALUES (6, 'Đà Nẵng');
INSERT INTO TourDestination VALUES (6, 'Hội An');

-- Tour 7 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hà Nội', 'Du lịch Nha Trang', 4, 3, 'assets/Image/Tour/Tour7.jpg', 2);
INSERT INTO TourDestination VALUES (7, 'Nha Trang');

-- Tour 8 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hồ Chí Minh', 'Du lịch Nha Trang', 4, 3, 'assets/Image/Tour/Tour7.jpg', 2);
INSERT INTO TourDestination VALUES (8, 'Nha Trang');

-- Tour 9 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Đà Nẵng', 'Du lịch Nha Trang', 4, 3, 'assets/Image/Tour/Tour7.jpg', 2);
INSERT INTO TourDestination VALUES (9, 'Nha Trang');

-- Tour 10 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hà Nội', 'Du lịch Phú Quốc', 4, 3, 'assets/Image/Tour/Tour9.jpg', 3);
INSERT INTO TourDestination VALUES (10, 'Phú Quốc');

-- Tour 11 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hà Nội', 'Xuyên Việt', 10, 9, 'assets/Image/Tour/Tour8.jpg', 2);
INSERT INTO TourDestination VALUES (11, 'Đà Nẵng');
INSERT INTO TourDestination VALUES (11, 'Hội An');
INSERT INTO TourDestination VALUES (11, 'Hạ Long');
INSERT INTO TourDestination VALUES (11, 'Sapa');
INSERT INTO TourDestination VALUES (11, 'Hà Nội');
INSERT INTO TourDestination VALUES (11, 'Cà Mau');
INSERT INTO TourDestination VALUES (11, 'Phú Quốc');

-- Tour 12 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hồ Chí Minh', 'Xuyên Việt', 10, 9, 'assets/Image/Tour/Tour8.jpg', 2);
INSERT INTO TourDestination VALUES (12, 'Đà Nẵng');
INSERT INTO TourDestination VALUES (12, 'Hội An');
INSERT INTO TourDestination VALUES (12, 'Hạ Long');
INSERT INTO TourDestination VALUES (12, 'Sapa');
INSERT INTO TourDestination VALUES (12, 'Hà Nội');
INSERT INTO TourDestination VALUES (12, 'Cà Mau');
INSERT INTO TourDestination VALUES (12, 'Phú Quốc');

-- Tour 13 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hồ Chí Minh', 'Du lịch Phú Quốc', 4, 3, 'assets/Image/Tour/Tour9.jpg', 3);
INSERT INTO TourDestination VALUES (13, 'Phú Quốc');

-- Tour 14 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Đà Nẵng', 'Du lịch Phú Quốc', 4, 3, 'assets/Image/Tour/Tour9.jpg', 3);
INSERT INTO TourDestination VALUES (14, 'Phú Quốc');


-- Tour 15 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hà Nội', 'Du lịch Đà Lạt', 4, 3, 'assets/Image/Tour/Tour11.jpg', 1);
INSERT INTO TourDestination VALUES (15, 'Đà Lạt');

-- Tour 16 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Đà Nẵng', 'Du lịch Đà Lạt', 4, 3, 'assets/Image/Tour/Tour11.jpg', 1);
INSERT INTO TourDestination VALUES (16, 'Đà Lạt');

-- Tour 17 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hồ Chí Minh', 'Du lịch Đà Lạt', 4, 3, 'assets/Image/Tour/Tour11.jpg', 1);
INSERT INTO TourDestination VALUES (17, 'Đà Lạt');

-- Tour 18 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Đà Nẵng', 'Du lịch Đà Nẵng - Hội An - Quảng Nam', 4, 3, 'assets/Image/Tour/Tour10.jpg', 2);
INSERT INTO TourDestination VALUES (18, 'Hội An');
INSERT INTO TourDestination VALUES (18, 'Đà Nẵng');

-- Tour 19 destinations
INSERT INTO Tour (start_destination, tour_name, day_number, night_number, link_image, admin_id_create_tour)
    VALUES('Hồ Chí Minh', 'Du lịch Đà Nẵng - Hội An - Quảng Nam', 4, 3, 'assets/Image/Tour/Tour10.jpg', 2);
INSERT INTO TourDestination VALUES (19, 'Đà Nẵng');
INSERT INTO TourDestination VALUES (19, 'Hội An');

COMMIT;

-- Add departure dates for each tour (3-5 per tour)
-- Tour 1 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) VALUES(1, TO_DATE('2025-05-05', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) VALUES(1, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) VALUES(1, TO_DATE('2025-05-20', 'YYYY-MM-DD'), 15, 15, 2700000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) VALUES(1, TO_DATE('2025-06-12', 'YYYY-MM-DD'), 25, 25, 3000000, 1);

-- Tour 2 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) VALUES(2, TO_DATE('2025-05-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) VALUES(2, TO_DATE('2025-06-20', 'YYYY-MM-DD'), 18, 18, 1500000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) VALUES(2, TO_DATE('2025-07-05', 'YYYY-MM-DD'), 12, 12, 1600000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) VALUES(2, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);

-- Tour 3 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(3, TO_DATE('2025-04-20', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(3, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(3, TO_DATE('2025-05-20', 'YYYY-MM-DD'), 15, 15, 2700000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(3, TO_DATE('2025-06-12', 'YYYY-MM-DD'), 25, 25, 3000000, 1);

-- Tour 4 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(4, TO_DATE('2025-06-20', 'YYYY-MM-DD'), 18, 18, 1500000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(4, TO_DATE('2025-07-05', 'YYYY-MM-DD'), 12, 12, 1600000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(4, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);


-- Tour 5 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(5, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(5, TO_DATE('2025-07-20', 'YYYY-MM-DD'), 15, 15, 2700000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(5, TO_DATE('2025-06-12', 'YYYY-MM-DD'), 25, 25, 3000000, 1);

-- Tour 6 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(6, TO_DATE('2025-06-20', 'YYYY-MM-DD'), 18, 18, 1500000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(6, TO_DATE('2025-07-05', 'YYYY-MM-DD'), 12, 12, 1600000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(6, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

-- Tour 7 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(7, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(7, TO_DATE('2025-05-20', 'YYYY-MM-DD'), 15, 15, 2700000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(7, TO_DATE('2025-06-12', 'YYYY-MM-DD'), 25, 25, 3000000, 1);

-- Tour 8 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(8, TO_DATE('2025-06-20', 'YYYY-MM-DD'), 18, 18, 1500000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(8, TO_DATE('2025-07-05', 'YYYY-MM-DD'), 12, 12, 1600000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(8, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);

-- Tour 9 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(9, TO_DATE('2025-04-15', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(9, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(9, TO_DATE('2025-05-20', 'YYYY-MM-DD'), 15, 15, 2700000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(9, TO_DATE('2025-06-12', 'YYYY-MM-DD'), 25, 25, 3000000, 1);

-- Tour 10 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(10, TO_DATE('2025-06-20', 'YYYY-MM-DD'), 18, 18, 1500000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(10, TO_DATE('2025-07-05', 'YYYY-MM-DD'), 12, 12, 1600000, 2);

-- Tour 11 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(11, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 20, 20, 2500000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(11, TO_DATE('2025-05-20', 'YYYY-MM-DD'), 15, 15, 2700000, 1);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(11, TO_DATE('2025-06-12', 'YYYY-MM-DD'), 25, 25, 3000000, 1);

-- Tour 12 departures
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(12, TO_DATE('2025-06-20', 'YYYY-MM-DD'), 18, 18, 1500000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(12, TO_DATE('2025-07-05', 'YYYY-MM-DD'), 12, 12, 1600000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(12, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(12, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

-- Tour 13 d
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(13, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(13, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

-- Tour 14 d
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(14, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(14, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(14, TO_DATE('2025-07-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

-- Tour 15 d
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(15, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(15, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(15, TO_DATE('2025-07-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

-- Tour 16 d
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(16, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(16, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(16, TO_DATE('2025-07-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

-- Tour 17 d
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(17, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(17, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(17, TO_DATE('2025-07-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

-- Tour 18 d
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(18, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(18, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(18, TO_DATE('2025-07-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

-- Tour 19 d
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(19, TO_DATE('2025-07-15', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(19, TO_DATE('2025-08-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(19, TO_DATE('2025-07-01', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(1, TO_DATE('2025-05-11', 'YYYY-MM-DD'), 20, 20, 1700000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(9, TO_DATE('2025-05-11', 'YYYY-MM-DD'), 15, 15, 1800000, 2);
INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain,  price, admin_id) VALUES(19, TO_DATE('2025-05-11', 'YYYY-MM-DD'), 15, 15, 1800000, 2);

COMMIT;
-- Continue similarly for tours 3-14 (abbreviated for space)
-- [Additional tour particulars would be inserted here...]

-- Create bookings (2-3 per user)
-- User 1 bookings
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(1, 1, TO_DATE('2025-05-05', 'YYYY-MM-DD'), 2, 5000000, 0);
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(1, 1, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 2, 5000000, 0);
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(1, 2, TO_DATE('2025-06-20', 'YYYY-MM-DD'), 1, 1500000, 0);
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(1, 3, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 1, 2500000, 0);

INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(2, 1, TO_DATE('2025-05-05', 'YYYY-MM-DD'), 2, 5000000, 0);
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(3, 1, TO_DATE('2025-05-05', 'YYYY-MM-DD'), 2, 5000000, 0);

INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(4, 1, TO_DATE('2025-05-05', 'YYYY-MM-DD'), 1, 2500000, 0);
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(5, 1, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 1, 2500000, 0);
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(4, 2, TO_DATE('2025-06-20', 'YYYY-MM-DD'), 1, 1500000, 0);
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(5, 3, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 1, 2500000, 0);

INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(6, 1, TO_DATE('2025-05-05', 'YYYY-MM-DD'), 2, 5000000, 0);
INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(6, 3, TO_DATE('2025-05-15', 'YYYY-MM-DD'), 2, 5000000, 0);

INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status) VALUES(7, 1, TO_DATE('2025-05-05', 'YYYY-MM-DD'), 1, 2500000, 0);

COMMIT;