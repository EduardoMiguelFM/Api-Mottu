-- V11__Add_status_based_descriptions.sql
-- Adicionar descrições baseadas no status da moto

-- Motos Disponíveis
UPDATE moto SET descricao = 'Pronta para uso' 
WHERE status = 'DISPONIVEL' AND descricao IS NULL;

-- Motos Reservadas
UPDATE moto SET descricao = 'Reservada para cliente' 
WHERE status = 'RESERVADA' AND descricao IS NULL;

-- Motos em Manutenção
UPDATE moto SET descricao = 'Em manutenção' 
WHERE status = 'MANUTENCAO' AND descricao IS NULL;

-- Motos com Falta de Peça
UPDATE moto SET descricao = 'Aguardando peças' 
WHERE status = 'FALTA_PECA' AND descricao IS NULL;

-- Motos Indisponíveis
UPDATE moto SET descricao = 'Temporariamente indisponível' 
WHERE status = 'INDISPONIVEL' AND descricao IS NULL;

-- Motos com Danos Estruturais
UPDATE moto SET descricao = 'Com danos estruturais' 
WHERE status = 'DANOS_ESTRUTURAIS' AND descricao IS NULL;

-- Motos com Sinistro
UPDATE moto SET descricao = 'Envolvida em sinistro' 
WHERE status = 'SINISTRO' AND descricao IS NULL;

-- Atualizar descrições existentes para serem mais simples
UPDATE moto SET descricao = 'Honda CG - Pronta para uso' 
WHERE status = 'DISPONIVEL' AND modelo LIKE '%CG%';

UPDATE moto SET descricao = 'Honda PCX - Pronta para uso' 
WHERE status = 'DISPONIVEL' AND modelo LIKE '%PCX%';

UPDATE moto SET descricao = 'Yamaha Fazer - Pronta para uso' 
WHERE status = 'DISPONIVEL' AND modelo LIKE '%Fazer%';

UPDATE moto SET descricao = 'Honda Biz - Pronta para uso' 
WHERE status = 'DISPONIVEL' AND modelo LIKE '%Biz%';

UPDATE moto SET descricao = 'Kawasaki Ninja - Pronta para uso' 
WHERE status = 'DISPONIVEL' AND modelo LIKE '%Ninja%';

-- Motos em manutenção
UPDATE moto SET descricao = 'Honda CG - Em manutenção' 
WHERE status = 'MANUTENCAO' AND modelo LIKE '%CG%';

UPDATE moto SET descricao = 'Honda PCX - Em manutenção' 
WHERE status = 'MANUTENCAO' AND modelo LIKE '%PCX%';

UPDATE moto SET descricao = 'Yamaha Fazer - Em manutenção' 
WHERE status = 'MANUTENCAO' AND modelo LIKE '%Fazer%';

-- Motos com problemas
UPDATE moto SET descricao = 'Honda CG - Com danos' 
WHERE status = 'DANOS_ESTRUTURAIS' AND modelo LIKE '%CG%';

UPDATE moto SET descricao = 'Honda PCX - Sinistro' 
WHERE status = 'SINISTRO' AND modelo LIKE '%PCX%';
