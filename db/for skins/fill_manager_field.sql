
set @manager_id       = '4';

UPDATE users SET manager_id = @manager_id WHERE role != 'manager';