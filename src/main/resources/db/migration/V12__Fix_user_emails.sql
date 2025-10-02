-- V12__Fix_user_emails.sql
-- Correção dos emails dos usuários para corresponder à página de login

UPDATE usuario_patio SET email = 'admin@teste.com' WHERE email = 'admin@mottu.com.br';
UPDATE usuario_patio SET email = 'supervisor@teste.com' WHERE email = 'supervisor@mottu.com.br';
UPDATE usuario_patio SET email = 'user@teste.com' WHERE email = 'operador@mottu.com.br';
