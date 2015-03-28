CREATE TABLE "GUEST"(
    "id" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "name" VARCHAR(50),
    "surname" VARCHAR(70),
    "address" VARCHAR(255),
    "phonenumber" VARCHAR(15)
)

CREATE TABLE "ROOM"(
    "id" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "floor" INT NOT NULL,
    "capacity" INT NOT NULL,
    "note" VARCHAR(255),
    "price" INT NOT NULL
)

CREATE TABLE "STAY"(
    "id" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "guest_id" BIGINT NOT NULL,
    "room_id" BIGINT NOT NULL,
    "start_of_stay" DATE NOT NULL,
    "end_of_stay" DATE NOT NULL,
    "total_price" INT NOT NULL 
)