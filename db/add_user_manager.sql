#
# Template for account creation.
#

set @username       = 'jdoe';
set @email          = 'jdoe@ussd.mobi';
set @full_name      = 'J. Doe';
set @password       = @username;
set @esdp_provider  = 'ipstest';      # `ipstest` - devel, 'ips' - production
set @esdp_login     = 'ipstest';      # `ipstest` - devel, 'ips' - production
set @esdp_password  = 'password_hash';   # MD5(password)

SELECT SHA2(@password, 256) INTO @pw;
INSERT INTO users
  (users_name, email, full_name, `password`, blocked, role, company, phone_number, esdp_provider, esdp_login, esdp_password)
VALUES
  (@username, @email, @full_name, @pw, 0, 'manager', NULL, NULL, @esdp_provider, @esdp_login, @esdp_password);
