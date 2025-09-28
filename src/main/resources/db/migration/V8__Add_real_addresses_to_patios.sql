-- V8__Add_real_addresses_to_patios.sql
-- Adicionar endereços reais dos pátios Mottu

UPDATE patio SET endereco = 'Rua Agostinho Leão, 1000 - Butantã, São Paulo - SP, 05515-000' WHERE nome = 'Pátio Butantã';
UPDATE patio SET endereco = 'Av. das Nações Unidas, 14171 - Vila Olímpia, São Paulo - SP, 04794-000' WHERE nome = 'Pátio Vila Olímpia';
UPDATE patio SET endereco = 'Rua Teodoro Sampaio, 3529 - Pinheiros, São Paulo - SP, 05405-000' WHERE nome = 'Pátio Pinheiros';
UPDATE patio SET endereco = 'Av. Paulista, 1000 - Bela Vista, São Paulo - SP, 01310-100' WHERE nome = 'Pátio Centro';
UPDATE patio SET endereco = 'Rua Augusta, 2000 - Consolação, São Paulo - SP, 01305-000' WHERE nome = 'Pátio Zona Sul';
