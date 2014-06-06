DELIMITER $$
DROP PROCEDURE IF EXISTS fill_subscribers;
CREATE PROCEDURE fill_subscribers(IN delivery INT)
  BEGIN
    DECLARE padding INT UNSIGNED DEFAULT 100000;

    DECLARE max INT UNSIGNED DEFAULT 100;
    DECLARE i INT UNSIGNED DEFAULT 0;

    START TRANSACTION;
    DELETE FROM delivery_subscribers WHERE delivery_id = delivery;
    WHILE i < max DO
      INSERT INTO delivery_subscribers (id, delivery_id, msisdn) VALUES
        (padding * delivery + i, delivery, (padding * delivery + i));
      SET i = i + 1;
    END WHILE;
    COMMIT;
  END $$

SET @id = 37;
CALL fill_subscribers(@id);
UPDATE deliveries SET state = 'ACTIVE' WHERE id = @id;