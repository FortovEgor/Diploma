DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,  -- length = 32? (in case of usage md5 for hasing)
    organization VARCHAR(100),
    avatar INT DEFAULT 0,
    about VARCHAR(300) DEFAULT 'hey I''m using Argus'
);

-- todo: create indexes