

INSERT INTO ui_profiles
  (id, icon, skin)
VALUES
  (null, null, 'MOBAK'),
  (null, null, 'MOBAK'),
  (null, null, 'MOBAK'),
  (null, null, 'ARAKS');




UPDATE users SET ui_profile_id = @profile_id WHERE id = @manager_id;
