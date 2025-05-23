-- DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    organization VARCHAR(100),
    avatar TEXT DEFAULT 'https://img.icons8.com/?size=100&id=z-JBA_KtSkxG&format=png&color=000000',
    about VARCHAR(300) DEFAULT 'hey I''m using Argus'
);