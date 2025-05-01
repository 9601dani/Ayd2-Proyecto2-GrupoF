CREATE TABLE comments(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    FK_User INT NOT NULL,
    FK_Case INT NOT NULL,
    id_comment_response INT,
    content TEXT
);