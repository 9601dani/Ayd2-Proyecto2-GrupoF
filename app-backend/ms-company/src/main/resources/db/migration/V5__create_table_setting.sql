CREATE TABLE settings(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    key_name VARCHAR(50) NOT NULL UNIQUE,
    key_value TEXT NOT NULL,
    value_type VARCHAR(100) NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE,
    FK_Setting_Type INT NOT NULL
);