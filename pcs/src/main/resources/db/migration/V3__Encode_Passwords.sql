CREATE extension if not exists pgcrypto;

UPDATE users SET password = crypt(password, gen_salt('bf', 8));