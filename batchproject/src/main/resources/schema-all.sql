DROP TABLE personas IF EXISTS;
CREATE TABLE personas (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(250),
    last_name VARCHAR(250),
    email VARCHAR(250),
    gender VARCHAR(10),
    ip_address VARCHAR(20)
);