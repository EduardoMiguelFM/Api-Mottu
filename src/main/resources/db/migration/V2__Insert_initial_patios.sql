-- V2__Insert_initial_patios.sql
-- Inserção de pátios iniciais para o sistema

INSERT INTO patio (nome) VALUES 
('Pátio Butantã'),
('Pátio Vila Olímpia'),
('Pátio Pinheiros'),
('Pátio Centro'),
('Pátio Zona Sul')
ON CONFLICT DO NOTHING;

