CREATE USER 'developer'@'%' IDENTIFIED WITH mysql_native_password BY '12345';
GRANT ALL PRIVILEGES ON board.* TO 'developer'@'%';

CREATE USER 'developer'@'localhost' IDENTIFIED WITH mysql_native_password BY '12345';
GRANT ALL PRIVILEGES ON board.* TO 'developer'@'localhost';

CREATE USER 'developer'@'_gateway' IDENTIFIED WITH mysql_native_password BY '12345';
GRANT ALL PRIVILEGES ON board.* TO 'developer'@'_gateway';

FLUSH PRIVILEGES;



