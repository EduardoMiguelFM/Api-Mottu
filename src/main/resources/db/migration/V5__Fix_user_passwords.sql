-- V5__Fix_user_passwords.sql
-- Correção das senhas dos usuários de teste

-- Atualizar senhas com hashes corretos
-- admin123 = $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi
-- user123 = $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW

UPDATE usuario_patio SET senha = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi' WHERE email = 'admin@mottu.com.br';
UPDATE usuario_patio SET senha = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' WHERE email = 'supervisor@mottu.com.br';
UPDATE usuario_patio SET senha = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' WHERE email = 'operador@mottu.com.br';

