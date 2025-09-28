-- V7__Add_endereco_to_patio.sql
-- Adicionar coluna endereco na tabela patio

ALTER TABLE patio ADD COLUMN endereco VARCHAR(255);

-- Atualizar registros existentes com endereço padrão
UPDATE patio SET endereco = 'Endereço não informado' WHERE endereco IS NULL;

-- Tornar a coluna obrigatória
ALTER TABLE patio ALTER COLUMN endereco SET NOT NULL;
