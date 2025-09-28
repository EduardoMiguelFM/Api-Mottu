-- V1__Create_tables.sql
-- Criação das tabelas principais do sistema MotoVision

-- Tabela de pátios
CREATE TABLE IF NOT EXISTS patio (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de usuários do pátio
CREATE TABLE IF NOT EXISTS usuario_patio (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    funcao VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de motos
CREATE TABLE IF NOT EXISTS moto (
    id BIGSERIAL PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    placa VARCHAR(7) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    setor VARCHAR(20) NOT NULL,
    cor_setor VARCHAR(20) NOT NULL,
    patio_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patio_id) REFERENCES patio(id) ON DELETE CASCADE
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_moto_placa ON moto(placa);
CREATE INDEX IF NOT EXISTS idx_moto_status ON moto(status);
CREATE INDEX IF NOT EXISTS idx_moto_setor ON moto(setor);
CREATE INDEX IF NOT EXISTS idx_moto_patio ON moto(patio_id);
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario_patio(email);
CREATE INDEX IF NOT EXISTS idx_usuario_cpf ON usuario_patio(cpf);

