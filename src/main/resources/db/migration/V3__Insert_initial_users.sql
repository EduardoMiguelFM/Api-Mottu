-- V3__Insert_initial_users.sql
-- Inserção de usuários iniciais com diferentes perfis

-- Senhas são: admin123 e user123 (hash bcrypt)
INSERT INTO usuario_patio (nome, cpf, funcao, email, senha, role) VALUES 
('Administrador Sistema', '12345678901', 'Administrador', 'admin@mottu.com.br', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN'),
('Supervisor Pátio', '98765432109', 'Supervisor', 'supervisor@mottu.com.br', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'SUPERVISOR'),
('Operador Pátio', '11122233344', 'Operador', 'operador@mottu.com.br', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'USER')
ON CONFLICT (email) DO NOTHING;

