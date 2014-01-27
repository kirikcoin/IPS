--
--  Initial data, required to be present for application to function properly.
--  Depends on database to be empty.
--

--
-- Creates system administrator, login: `admin', password: `admin'.
--

INSERT INTO users (`id`, `email`, `full_name`, `users_name`, `password`, `blocked`, `role`)
VALUES (1,
        'username@example.com',
        'Default Admin',
        'admin',
        '8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918',
        FALSE,
        'admin');
