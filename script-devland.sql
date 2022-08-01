create schema devland;

create table usuario (
	  id_usuario 		numeric 	not null,
	  nome 				text 		not null,
	  email 			text 		not null,	  	  
	  area_atuacao 		text 		not null,
	  cpf_cnpj			text 		not null unique,
	  foto 				text		not null,
	  genero		    text		not null,
	  tipo  		    text		not null,
	  primary key (id_usuario)	  
);

create table tecnologias (
	  id_tecnologia		numeric 	not null,
	  id_usuario		numeric		not null,
	  nome_tecnologia	text 		not null, 
	  primary key (id_tecnologia),
	  constraint fk_tecnologias_usuario
	  	foreign key (id_usuario)
	  		references usuario(id_usuario) on delete cascade
);

create table endereco (
	  id_endereco 		numeric 	not null,	  
	  tipo 				text 		not null,
	  logradouro 		text 		not null,
	  numero 			text 		not null,
	  complemento 		text 		not null,
	  cep 				text		not null,
	  cidade 			text 		not null,
	  estado 			text 		not null,
	  pais 				text 		not null,
	  primary key (id_endereco)	  
);

create table usuario_x_endereco (
	  id_usuario 		numeric 	not null,
	  id_endereco		numeric 	not null,
	  primary key(id_usuario, id_endereco),
	  constraint fk_x_usuario
	  	foreign key(id_usuario)
	  		references usuario(id_usuario) on delete cascade,
	  constraint fk_x_endereco
	  	foreign key (id_endereco)
	  		references endereco(id_endereco) on delete cascade
);

create table contato (
	  id_contato 		numeric 	not null,
	  id_usuario 		numeric 	not null,
	  tipo 				text 		not null,
	  numero 			text 		not null,	
	  descricao 		text		not null,
	  primary key (id_contato),
	  constraint fk_contato_usuario
	    foreign KEY (id_usuario)
	      references usuario(id_usuario) on delete cascade
);

create table seguidor (
	  id 				numeric 	not null,
	  id_seguidor 		numeric 	not null,
	  nome_seguidor		text 		not null,
	  id_usuario 		numeric 	not null,	  
	  primary key (id),
	  constraint fk_seguidor_usuario
	    foreign KEY (id_usuario)
	      references usuario(id_usuario) on delete cascade
);

create table postagem (
	  id_postagem 		numeric 	not null,
	  id_usuario 		numeric 	not null,
	  tipo 				text 		not null,
	  titulo 			text 		not null,
	  descricao			text 		not null,	
	  foto				text 		not null,
	  curtidas			numeric		not null,	  	  
	  data_postagem		timestamp	not null,
	  primary key (id_postagem),
	  constraint fk_postagem_usuario
	    foreign KEY (id_usuario)
	      references usuario(id_usuario) on delete cascade
);

create table comentario (
	  id_comentario		numeric		not null,	  
	  id_usuario 		numeric 	not null,	  
	  id_postagem 		numeric 	not null,
	  descricao			text 		not null,	
	  curtidas			numeric		not null,	    
	  data_comentario	timestamp	not null,
	  primary key (id_comentario),
	  constraint fk_comentario_usuario
	    foreign KEY (id_usuario)
	      references usuario(id_usuario) on delete cascade,
	  constraint fk_comentario_postagem
	    foreign KEY (id_postagem)
	      references postagem(id_postagem) on delete cascade
);

create sequence seq_usuario
increment 1
start 1;

create sequence seq_tecnologias
increment 1
start 1;

create sequence seq_endereco
increment 1
start 1;

create sequence seq_contato
increment 1
start 1;

create sequence seq_seguidor
increment 1
start 1;

create sequence seq_postagem
increment 1
start 1;

create sequence seq_comentario
increment 1
start 1;