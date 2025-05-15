
INSERT INTO pages (id, name, path, FK_Module)
VALUES
    (7, 'Reportes', '/reports', 4);

INSERT INTO roles_has_pages(FK_Role, FK_Page)
VALUES
    (1, 7);