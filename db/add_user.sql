#
# Template for account creation.
#

set @username   = 'jdoe';
set @email      = 'jdoe@ussd.mobi';
set @full_name  = 'J. Doe';
set @password   = @username;
set @role       = 'manager';
# set @role       = 'client';

SELECT SHA2(@password, 256) INTO @pw;
INSERT INTO users
  (users_name, email, full_name, `password`, blocked, role, company, phone_number)
VALUES
  (@username, @email, @full_name, @pw, 0, @role, NULL, NULL);
