insert into usuarios (id, username, password, role) values (100, 'ana@gmail.com', '$2a$10$jTA9/NKrBRQJ9B7FdfJzzeymVBZ.bBhW7igl4gvBaLd3q3BfiP.O2', 'ROLE_ADMIN');
insert into usuarios (id, username, password, role) values (101, 'kelvin@gmail.com', '$2a$10$jTA9/NKrBRQJ9B7FdfJzzeymVBZ.bBhW7igl4gvBaLd3q3BfiP.O2', 'ROLE_CLIENTE');
insert into usuarios (id, username, password, role) values (102, 'vitor@gmail.com', '$2a$10$jTA9/NKrBRQJ9B7FdfJzzeymVBZ.bBhW7igl4gvBaLd3q3BfiP.O2', 'ROLE_CLIENTE');

insert into clientes (id, nome, cpf, id_usuario) values (21, 'Kelvin Martins', '98133219035', 101);
insert into clientes (id, nome, cpf, id_usuario) values (22, 'Vitor Alves', '99012561000', 102);

insert into vagas (id, codigo, status) values (100, 'A-01', 'OCUPADA');
insert into vagas (id, codigo, status) values (200, 'A-02', 'OCUPADA');
insert into vagas (id, codigo, status) values (300, 'A-03', 'OCUPADA');
insert into vagas (id, codigo, status) values (400, 'A-04', 'LIVRE');
insert into vagas (id, codigo, status) values (500, 'A-05', 'LIVRE');

insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
		values ('20250313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2025-03-13 10:15:00', 22, 100);
insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
		values ('20250314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2025-03-14 10:15:00', 21, 200);
insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
		values ('20250315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2025-03-14 10:15:00', 22, 300);