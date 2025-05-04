TRUNCATE TABLE modules;

INSERT INTO modules(id, name, path)
VALUES (1, 'Administración', '/admin'),
       (2, 'Proyectos', '/projects'),
       (3, 'Configuración', '/settings'),
       (4, 'Reportes', '/reports');