-- V10__Add_descriptions_to_existing_motos.sql
-- Adicionar descrições nas motos existentes

UPDATE moto SET descricao = 'Moto Honda CG em excelente estado, revisada recentemente' WHERE modelo LIKE '%CG%';
UPDATE moto SET descricao = 'Moto Honda PCX 160cc, baixa quilometragem, pronta para uso' WHERE modelo LIKE '%PCX%';
UPDATE moto SET descricao = 'Moto Yamaha Fazer 250cc, motor em perfeito funcionamento' WHERE modelo LIKE '%Fazer%';
UPDATE moto SET descricao = 'Moto Honda Biz 125cc, econômica e confiável' WHERE modelo LIKE '%Biz%';
UPDATE moto SET descricao = 'Moto Kawasaki Ninja 300cc, esportiva e ágil' WHERE modelo LIKE '%Ninja%';
UPDATE moto SET descricao = 'Moto em bom estado geral, revisada e pronta para uso' WHERE descricao IS NULL;
