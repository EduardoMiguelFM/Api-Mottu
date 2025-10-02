-- =============================================
-- Mottu API Database Script
-- Sistema de Gestão de Motos, Pátios e Usuários
-- Challenge FIAP 2025 - DevOps Tools & Cloud Computing
-- Azure Database for PostgreSQL
-- =============================================

-- Tabela de Pátios
CREATE TABLE IF NOT EXISTS patios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    funcao VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Motos
CREATE TABLE IF NOT EXISTS motos (
    id BIGSERIAL PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    placa VARCHAR(10) UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL,
    setor VARCHAR(10) NOT NULL,
    cor_setor VARCHAR(20) NOT NULL,
    descricao VARCHAR(255),
    patio_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patio_id) REFERENCES patios(id)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_motos_status ON motos(status);
CREATE INDEX IF NOT EXISTS idx_motos_patio ON motos(patio_id);
CREATE INDEX IF NOT EXISTS idx_motos_placa ON motos(placa);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);

-- Dados iniciais - Pátios
INSERT INTO patios (nome, endereco) VALUES 
('Pátio Butantã', 'Rua das Flores, 123 - Butantã, São Paulo - SP'),
('Pátio Vila Madalena', 'Av. Paulista, 456 - Vila Madalena, São Paulo - SP'),
('Pátio Pinheiros', 'Rua Augusta, 789 - Pinheiros, São Paulo - SP')
ON CONFLICT DO NOTHING;

-- Dados iniciais - Usuários
INSERT INTO usuarios (nome, email, senha, cpf, funcao) VALUES 
('Admin Sistema', 'admin@mottu.com.br', 'admin123', '12345678901', 'Administrador'),
('Supervisor Operacional', 'supervisor@mottu.com.br', 'admin123', '12345678902', 'Supervisor'),
('Operador Campo', 'operador@mottu.com.br', 'admin123', '12345678903', 'Operador')
ON CONFLICT DO NOTHING;

-- Dados iniciais - Motos
INSERT INTO motos (modelo, placa, status, setor, cor_setor, descricao, patio_id) VALUES 
('Honda Biz 125', 'ABC-1234', 'DISPONIVEL', 'Setor A', 'Verde', 'Moto em perfeito estado', 1),
('Yamaha Factor 150', 'DEF-5678', 'MANUTENCAO', 'Setor C', 'Amarelo', 'Manutenção preventiva', 1),
('Honda CG 160', 'GHI-9012', 'RESERVADA', 'Setor B', 'Azul', 'Reservada para locação', 2)
ON CONFLICT DO NOTHING;

-- Comentários das tabelas
COMMENT ON TABLE patios IS 'Tabela de pátios onde as motos ficam estacionadas';
COMMENT ON TABLE usuarios IS 'Tabela de usuários funcionários do sistema';
COMMENT ON TABLE motos IS 'Tabela principal de motos com status e localização';

-- Comentários das colunas principais
COMMENT ON COLUMN motos.status IS 'Status da moto: DISPONIVEL, RESERVADA, MANUTENCAO, FALTA_PECA, INDISPONIVEL, DANOS_ESTRUTURAIS, SINISTRO';
COMMENT ON COLUMN motos.setor IS 'Setor definido automaticamente baseado no status';
COMMENT ON COLUMN motos.cor_setor IS 'Cor definida automaticamente baseada no status';
COMMENT ON COLUMN motos.patio_id IS 'Referência para o pátio onde a moto está localizada';
