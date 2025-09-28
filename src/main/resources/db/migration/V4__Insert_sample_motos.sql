-- V4__Insert_sample_motos.sql
-- Inserção de motos de exemplo para demonstração

INSERT INTO moto (modelo, placa, status, setor, cor_setor, patio_id) VALUES 
('Honda Biz', 'ABC1234', 'DISPONIVEL', 'Setor A', 'Verde', 1),
('Yamaha Factor', 'DEF5678', 'RESERVADA', 'Setor B', 'Azul', 1),
('Honda CG 160', 'GHI9012', 'MANUTENCAO', 'Setor C', 'Amarelo', 2),
('Yamaha Fazer', 'JKL3456', 'FALTA_PECA', 'Setor D', 'Laranja', 2),
('Honda PCX', 'MNO7890', 'INDISPONIVEL', 'Setor E', 'Cinza', 3),
('Yamaha NMAX', 'PQR1234', 'DANOS_ESTRUTURAIS', 'Setor F', 'Vermelho', 3),
('Honda CB 600', 'STU5678', 'SINISTRO', 'Setor G', 'Preto', 4),
('Yamaha MT-07', 'VWX9012', 'DISPONIVEL', 'Setor A', 'Verde', 4),
('Honda XRE 300', 'YZA3456', 'RESERVADA', 'Setor B', 'Azul', 5),
('Yamaha R3', 'BCD7890', 'MANUTENCAO', 'Setor C', 'Amarelo', 5)
ON CONFLICT (placa) DO NOTHING;

