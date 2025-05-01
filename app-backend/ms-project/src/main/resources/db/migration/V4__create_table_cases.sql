CREATE TABLE cases(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    FK_Project INT NOT NULL,
    progress_percentage DECIMAL(10, 2) DEFAULT 0.00,
    FK_Case_Phase INT NOT NULL,
    limit_date DATETIME NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE
);