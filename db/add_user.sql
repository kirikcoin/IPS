#
# Template for account creation.
#

set @username   = 'vit';
set @email      = 'vit@eyeline.mobi';
set @full_name  = 'Виталий Гумиров';
set @password   = @username;
set @role       = 'manager';
# set @role       = 'client';

SELECT SHA2(@password, 256) INTO @pw;
INSERT INTO users
  (users_name, email, full_name, `password`, blocked, role, company, phone_number)
VALUES
  (@username, @email, @full_name, @pw, 0, @role, NULL, NULL);
