CREATE TABLE IF NOT EXISTS USERS(
    username          VARCHAR(100) PRIMARY KEY,
    password          VARCHAR(100) NOT NULL,
    jwt               VARCHAR(100),
    lastActivity      DATE
);


CREATE TABLE IF NOT EXISTS ROLES(
    role              VARCHAR(100) PRIMARY KEY,
    Description       VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS USER_ROLE(
    username          VARCHAR(100) NOT NULL,
    role              VARCHAR(100) NOT NULL,
    PRIMARY KEY (username, role)
);