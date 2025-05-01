CREATE TABLE history_case_phases(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    FK_Case INT NOT NULL,
    FK_User INT NOT NULL,
    FK_Case_Phase INT NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    time_spent DECIMAL(10, 2) DEFAULT 0.00
);