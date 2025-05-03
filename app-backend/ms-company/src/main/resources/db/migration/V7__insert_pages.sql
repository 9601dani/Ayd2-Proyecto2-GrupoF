INSERT INTO pages (id, name, path, FK_Module)
    VALUES
        (1, 'Home', '/home', 1),
        (2, 'Usuarios', '/users', 2),
        (3, 'Crear Usuario', '/users/create', 2),
        (4, 'Perfil Personal', '/users/profile', 2),
        (5, 'Crear Proyecto', '/projects/create', 3),
        (6, 'Mis Proyectos', '/projects/my-projects', 3),
        (7, 'Proyectos', '/projects', 3),
        (8, 'Tipos de Caso', '/case-types', 4),
        (9, 'Crear Tipo de Caso', '/case-types/create', 4),
        (10, 'Reportes 1', '/reports1', 5),
        (11, 'Inicio', '/home', 6),
        (12, 'Mi Perfil', '/home', 6),
        (13, 'Mis Casos', '/my-cases', 7)