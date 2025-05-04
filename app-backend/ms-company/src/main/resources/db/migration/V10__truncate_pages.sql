TRUNCATE TABLE pages;

INSERT INTO pages (id, name, path, FK_Module)
VALUES
    (1, 'Manejo de usuarios', '/users', 1),
    (2, 'Creación de casos', '/cases-creation', 1),
    (3, 'Proyectos activos', '/projects', 2),
    (4, 'Proyectos congelados', '/disabled-projects', 2),
    (5, 'Casos', '/cases', 2),
    (6, 'Configuraciones de la empresa', '/company-settings', 3);