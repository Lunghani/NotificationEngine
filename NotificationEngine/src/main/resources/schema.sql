CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50),
    password VARCHAR(100),
    email VARCHAR(100),
    notify BOOLEAN
    );