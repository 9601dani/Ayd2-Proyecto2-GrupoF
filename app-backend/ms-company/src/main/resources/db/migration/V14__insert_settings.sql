INSERT INTO settings(key_name, key_value, value_type, is_enabled, FK_Setting_Type)
VALUES ('company_name', 'CodenBugs', 'text', TRUE, 1),
       ('company_address', 'CIUDAD', 'text', TRUE, 1),
       ('company_phone_number', '12345678', 'text', TRUE, 1),
       ('company_email_address', 'encodersnoreply@gmail.com;', 'email', TRUE, 2),
       ('email_password', 'qvns abya uamf gwpk', 'password', TRUE, 3),
       ('company_logo', '/images/logo.jpeg', 'image', TRUE, 4),
       ('company_currency', 'Q', 'text', TRUE, 4);