CREATE TABLE users
(
    id         serial8 NOT NULL,
    active     boolean,
    email      varchar(255) NOT NULL,
    "name"     varchar(255) NOT NULL,
    "password" varchar(255) NOT NULL,
    rating     float4,
    UNIQUE (name),
    UNIQUE (email),
    PRIMARY KEY (id)
);
CREATE TABLE roles
(
    id     serial8 NOT NULL,
    "name" varchar(255),
    PRIMARY KEY (id)
);
CREATE TABLE ad
(
    id            serial8 NOT NULL,
    category      int4,
    creation_date timestamp,
    premium       boolean,
    price         real,
    "text"        varchar(4096),
    title         varchar(255),
    user_id       int8 REFERENCES users (id),
    PRIMARY KEY (id)
);

CREATE TABLE "comment"
(
    id            serial8 NOT NULL,
    creation_date timestamp,
    "text"        varchar(2048),
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
    "text"         varchar(2048),
    message_to     int8 REFERENCES users (id),
    message_from   int8 REFERENCES users (id),
    PRIMARY KEY (id)
);
CREATE TABLE refreshtoken
(
    id          serial8 NOT NULL,
    expiry_date timestamp NOT NULL,
    "token"     varchar(255) NOT NULL,
    user_id     int8 NOT NULL REFERENCES users (id),
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
    id        serial8 NOT NULL,
    chat_id   varchar(255) NULL,
    recipient varchar(255) NULL,
    sender    varchar(255) NULL,
    PRIMARY KEY (id)
);
CREATE TABLE verification_token
(
    id          serial8 NOT NULL,
    expiry_date timestamp,
    "token"     varchar(255),
    user_id     int8 NOT NULL REFERENCES users (id),
    PRIMARY KEY (id)
);
-- ALTER TABLE public.verification_token
--     ADD CONSTRAINT verification_token_users_fk FOREIGN KEY (user_id) REFERENCES users (id);