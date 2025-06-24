-- Kết nối đến PDB2 và đặt schema
ALTER SESSION SET CONTAINER = PDB2;
ALTER SESSION SET CURRENT_SCHEMA = PDB2_ADMIN;

-- Tạo bảng UserTable
CREATE TABLE UserTable (
    user_id NUMBER GENERATED ALWAYS AS IDENTITY,
    user_name NVARCHAR2(200),
    email NVARCHAR2(100) UNIQUE,
    phone_number NVARCHAR2(20) UNIQUE,
    user_password NVARCHAR2(100),
    user_address NVARCHAR2(200),
    is_admin NUMBER(1) DEFAULT 0 CHECK (is_admin IN (0, 1)),
    PRIMARY KEY(user_id)
);

-- Tạo bảng AdminTable
CREATE TABLE AdminTable(
    admin_id NUMBER PRIMARY KEY,
    user_id NUMBER,
    FOREIGN KEY (user_id) REFERENCES UserTable(user_id)
);

-- Tạo bảng Tour
CREATE TABLE Tour (
    tour_id NUMBER GENERATED ALWAYS AS IDENTITY,
    start_destination NVARCHAR2(200),
    tour_name NVARCHAR2(500),
    day_number INT,
    night_number INT,
    link_image NVARCHAR2(200),
    admin_id_create_tour NUMBER REFERENCES AdminTable(admin_id),
    daytime_create_tour DATE DEFAULT SYSDATE,
    PRIMARY KEY(tour_id)
);

-- Tạo bảng TourDestination
CREATE TABLE TourDestination (
    tour_id NUMBER REFERENCES Tour(tour_id),
    thoughout_destination NVARCHAR2(200),
    PRIMARY KEY (tour_id, thoughout_destination)
);

-- Tạo bảng TourParticular
CREATE TABLE TourParticular(
    tour_id NUMBER REFERENCES Tour(tour_id),
    daytime_start DATE,
    total_slot INT,
    slot_remain INT,
    price INT,
    status INT DEFAULT 0 CHECK(status IN (0,1,2,3)),
    admin_id NUMBER REFERENCES AdminTable(admin_id),
    daytime_create DATE DEFAULT SYSDATE,
    PRIMARY KEY (tour_id, daytime_start)
);

-- Tạo bảng Booking
CREATE TABLE Booking(
    booking_id NUMBER GENERATED ALWAYS AS IDENTITY,
    user_id NUMBER REFERENCES UserTable(user_id),
    tour_id NUMBER,
    daytime_start DATE,
    slot_reserved INT,
    total_purchase INT,
    daytime_create DATE DEFAULT SYSDATE,
    status INT CHECK(status IN (0,1,2,3)),
    FOREIGN KEY (tour_id, daytime_start) REFERENCES TourParticular(tour_id, daytime_start),
    PRIMARY KEY(booking_id)
);

-- Tạo bảng Notice
CREATE TABLE Notice(
    booking_id NUMBER REFERENCES Booking(booking_id),
    user_id NUMBER REFERENCES UserTable(user_id),
    notice_id NUMBER GENERATED ALWAYS AS IDENTITY,
    notice_type NUMBER,
    tour_id NUMBER,
    daytime_start DATE,
    status INT DEFAULT 0 CHECK(status IN (0,1)),
    FOREIGN KEY (tour_id, daytime_start) REFERENCES TourParticular(tour_id, daytime_start),
    PRIMARY KEY(notice_id)
);

COMMIT;