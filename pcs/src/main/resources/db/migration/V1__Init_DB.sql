CREATE TABLE users
(
    id       serial8      NOT NULL,
    active   boolean,
    email    varchar(255) NOT NULL,
    name     varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    UNIQUE (name),
    UNIQUE (email),
    PRIMARY KEY (id)
);
CREATE TABLE rating
(
    id      serial8 NOT NULL,
    score   float4,
    user_id int8 REFERENCES users (id),
    PRIMARY KEY (id)
);
CREATE TABLE roles
(
    id   serial8 NOT NULL,
    name varchar(255),
    PRIMARY KEY (id)
);
CREATE TABLE ad
(
    id            serial8 NOT NULL,
    category      int4,
    creation_date timestamp,
    premium       boolean,
    price         real,
    text          varchar(4096),
    title         varchar(255),
    user_id       int8 REFERENCES users (id),
    PRIMARY KEY (id)
);
CREATE TABLE comment
(
    id            serial8 NOT NULL,
    creation_date timestamp,
    text          varchar(2048),
    ad_id         int8 REFERENCES ad (id),
    user_id       int8 REFERENCES users (id),
    PRIMARY KEY (id)
);
CREATE TABLE message
(
    id             serial8 NOT NULL,
    chat_id        varchar(255),
    creation_date  timestamp,
    message_status int4,
    text           varchar(2048),
    message_to     int8 REFERENCES users (id),
    message_from   int8 REFERENCES users (id),
    PRIMARY KEY (id)
);
CREATE TABLE refreshtoken
(
    id          serial8      NOT NULL,
    expiry_date timestamp    NOT NULL,
    token       varchar(255) NOT NULL,
    user_id     int8         NOT NULL REFERENCES users (id),
    PRIMARY KEY (id),
    UNIQUE (token)
);
CREATE TABLE user_roles
(
    user_id int8 NOT NULL REFERENCES users (id),
    role_id int8 NOT NULL REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);
CREATE TABLE chat_room
(
    id        serial8      NOT NULL,
    chat_id   varchar(255) NULL,
    recipient int8 NULL,
    sender    int8 NULL,
    ad_id     int8 REFERENCES ad (id),
    PRIMARY KEY (id)
);
CREATE TABLE verification_token
(
    id          serial8 NOT NULL,
    expiry_date timestamp,
    token       varchar(255),
    user_id     int8    NOT NULL REFERENCES users (id),
    PRIMARY KEY (id)
);