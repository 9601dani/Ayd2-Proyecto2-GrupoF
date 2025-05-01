CREATE TABLE case_phases(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    FK_Case_Types INT NOT NULL,
    next_phase INT NULL
);