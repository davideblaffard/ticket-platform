INSERT INTO tickets (title, description, ticket_status, operator_id, category_id) VALUES ('Errore login', 'utente non riesce ad accedere con le credenziali corrette', 'TO_DO', 1, 2),('Schermata blu', 'Il PC va in crash dopo avvio', 'ON_GOING', 2, 3),('Aggiornamento software', 'Richiesta di update alla versione più recente', 'COMPLETED', 3, 1),('Problema stampante', 'La stampante non risponde ai comandi', 'TO_DO', 4, 2),('Errore database', 'Connessione al database fallita', 'ON_GOING', 1, 3),('Reset password', 'utente ha dimenticato la password', 'COMPLETED', 5, 2),('Accesso negato', 'Il sistema blocca un utente autorizzato', 'TO_DO', 3, 4),('Bug nell app mobile', 'Il pulsante Invia non funziona su iOS', 'ON_GOING', 4, 1),('Problema con VPN', 'La connessione VPN cade frequentemente', 'COMPLETED', 5, 3),('Richiesta nuovo account', 'Creazione account per un nuovo dipendente', 'TO_DO', 2, 2),('Errore pagamento', 'Il pagamento con carta di credito fallisce', 'ON_GOING', 3, 4),('Impossibile installare app', 'Errore durante l installazione del software aziendale', 'COMPLETED', 4, 1),('Connessione lenta', 'Il WiFi aziendale è molto lento', 'TO_DO', 5, 3),('Configurazione email', 'L utente non riceve email in Outlook', 'ON_GOING', 2, 2),('Problema con il server', 'Il server non risponde ai ping', 'COMPLETED', 3, 1);

INSERT INTO operators (username, email, password, not_available, role) VALUES ('admin1', 'admin1@example.com', 'admin123', false, 'ADMIN'),('operator_john', 'john.doe@example.com', 'password123', false, 'OPERATOR'),('operator_jane', 'jane.doe@example.com', 'securepass', true, 'OPERATOR'),('operator_mike', 'mike.smith@example.com', 'mikepass', false, 'OPERATOR'),('operator_emily', 'emily.jones@example.com', 'emilypass', true, 'OPERATOR');

INSERT INTO notes (content, created_on, operator_id, ticket_id) VALUES ('L utente ha segnalato il problema questa mattina.', '2025-03-20', 2, 1),('Abbiamo riavviato il server, il problema sembra risolto.', '2025-03-20', 3, 5),('In attesa di ulteriori dettagli dal cliente.', '2025-03-19', 4, 3),('Ho aggiornato i driver della stampante, ora funziona.', '2025-03-18', 1, 4),('Richiesto log degli errori per analisi approfondita.', '2025-03-17', 2, 2),('Abbiamo fornito una soluzione temporanea.', '2025-03-16', 3, 6),('Ticket passato al livello superiore di assistenza.', '2025-03-15', 4, 7),('Il problema si è ripresentato dopo il riavvio.', '2025-03-14', 1, 8),('Utente non disponibile per ulteriori test.', '2025-03-13', 2, 9),('Ticket chiuso, il cliente ha confermato la risoluzione.', '2025-03-12', 3, 10);

INSERT INTO categories (category_name) VALUES ('Software Issues'),('Hardware Malfunctions'),('Network Problems'),('Account & Access'),('General Inquiries');
