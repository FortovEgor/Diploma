DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,  -- length = 32? (in case of usage md5 for hasing)
    organization VARCHAR(100),
    avatar TEXT DEFAULT 'https://img.icons8.com/?size=100&id=z-JBA_KtSkxG&format=png&color=000000',
    about VARCHAR(300) DEFAULT 'hey I''m using Argus'
);

-- todo: create indexes