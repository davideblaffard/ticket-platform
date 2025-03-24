-- OPERATORS
INSERT INTO operators (username, email, password, not_available, role, enable) VALUES('admin1', 'admin1@example.com', '{noop}admin123', false, 'ADMIN', true),('operator_john', 'john.doe@example.com', '{noop}password123', false, 'OPERATOR', true),('operator_jane', 'jane.doe@example.com', '{noop}securepass', true, 'OPERATOR', true),('operator_mike', 'mike.smith@example.com', '{noop}mikepass', false, 'OPERATOR', true);

-- CATEGORIES
INSERT INTO categories (category_name) VALUES('Assistenza tecnica'),('Software'),('Hardware'),('Account e Accessi');

-- TICKETS
INSERT INTO tickets (title, description, ticket_status, operator_id, category_id) VALUES('Errore login', 'Utente non riesce ad accedere', 'TO_DO', 2, 4),('Crash sistema', 'Schermata blu all avvio', 'ON_GOING', 3, 3),('Reset password', 'Password dimenticata', 'COMPLETED', 4, 4),('Stampante offline', 'La stampante non risponde', 'TO_DO', 2, 3),('Aggiornamento software', 'Richiesta aggiornamento app', 'ON_GOING', 3, 2),('VPN instabile', 'Connessione VPN intermittente', 'COMPLETED', 4, 1);

-- NOTES
INSERT INTO notes (content, created_on, operator_id, ticket_id) VALUES('Verificata la richiesta', '2025-03-20', 2, 1),('Problema ancora in corso', '2025-03-21', 3, 2),('Richiesta completata correttamente', '2025-03-22', 4, 3);