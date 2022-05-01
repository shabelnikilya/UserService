CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    first_name TEXT,
    second_name TEXT,
    age INT,
    roles TEXT
);