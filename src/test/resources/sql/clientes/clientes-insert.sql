insert into USUARIOS (id, username, password, role) values (100, 'ana@gmail.com', '$2a$10$jTA9/NKrBRQJ9B7FdfJzzeymVBZ.bBhW7igl4gvBaLd3q3BfiP.O2', 'ROLE_ADMIN');
insert into USUARIOS (id, username, password, role) values (101, 'kelvin@gmail.com', '$2a$10$jTA9/NKrBRQJ9B7FdfJzzeymVBZ.bBhW7igl4gvBaLd3q3BfiP.O2', 'ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role) values (102, 'vitor@gmail.com', '$2a$10$jTA9/NKrBRQJ9B7FdfJzzeymVBZ.bBhW7igl4gvBaLd3q3BfiP.O2', 'ROLE_CLIENTE');
insert into USUARIOS (id, username, password, role) values (103, 'toby@gmail.com', '$2a$10$jTA9/NKrBRQJ9B7FdfJzzeymVBZ.bBhW7igl4gvBaLd3q3BfiP.O2', 'ROLE_CLIENTE');

insert into CLIENTES (id, nome, cpf, id_usuario) values (10, 'Kelvin Martins', '98133219035', 101);
insert into CLIENTES (id, nome, cpf, id_usuario) values (20, 'Vitor Alves', '99012561000', 102);