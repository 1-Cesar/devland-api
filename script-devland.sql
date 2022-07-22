create sequence DEVLAND

CREATE TABLE Usuario (
	  id_usuario 		NUMERIC 	NOT NULL,
	  nome 				TEXT 		NOT NULL,
	  email 			TEXT 		NOT NULL,	  	  
	  area_atuacao 		TEXT 		NOT NULL,
	  cpf_cnpj			TEXT 		NOT NULL UNIQUE,
	  tipo 				TEXT		NOT NULL,
	  foto 				TEXT		NOT NULL,
	  PRIMARY KEY (id_usuario)	  
);

CREATE TABLE Seguidor (
	  id 				NUMERIC 	NOT NULL,
	  id_seguidor 		NUMERIC 	NOT NULL,
	  nome_seguidor		TEXT 		NOT NULL,
	  id_usuario 		NUMERIC 	NOT NULL,	  
	  PRIMARY KEY (id),
	  CONSTRAINT FK_Seguidor_id_seguidor
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario)
);

CREATE TABLE Endereco (
	  id_endereco 		NUMERIC 	NOT NULL,
	  id_usuario 		NUMERIC 	NOT NULL,
	  tipo 				TEXT 		NOT NULL,
	  logradouro 		TEXT 		NOT NULL,
	  numero 			TEXT 		NOT NULL,
	  complemento 		TEXT 		NOT NULL,
	  cep 				TEXT		NOT NULL,
	  cidade 			TEXT 		NOT NULL,
	  estado 			TEXT 		NOT NULL,
	  pais 				TEXT 		NOT NULL,
	  PRIMARY KEY (id_endereco),
	  CONSTRAINT FK_Contato_id_endereco
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario)
);

CREATE TABLE Contato (
	  id_contato 		NUMERIC 	NOT NULL,
	  id_usuario 		NUMERIC 	NOT NULL,
	  tipo 				TEXT 		NOT NULL,
	  numero 			TEXT 		NOT NULL,	
	  descricao 		TEXT		NOT NULL,
	  PRIMARY KEY (id_contato),
	  CONSTRAINT FK_Contato_id_contato
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario)
);

CREATE TABLE Postagem (
	  id_postagem 		NUMERIC 	NOT NULL,
	  id_usuario 		NUMERIC 	NOT NULL,
	  tipo 				TEXT 		NOT NULL,
	  titulo 			TEXT 		NOT NULL,
	  descricao			TEXT 		NOT NULL,	
	  ups				NUMERIC		NOT NULL,
	  downs				NUMERIC		NOT NULL,
	  views				NUMERIC		NOT NULL,
	  data_postagem		TIMESTAMP	NOT NULL,
	  PRIMARY KEY (id_postagem),
	  CONSTRAINT FK_Postagem_id_usuario
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario)
);

CREATE TABLE Comentario (
	  id_comentario		NUMERIC		NOT NULL,	  
	  id_usuario 		NUMERIC 	NOT NULL,	  
	  id_postagem 		NUMERIC 	NOT NULL,
	  descricao			TEXT 		NOT NULL,	
	  ups				NUMERIC		NOT NULL,
	  downs				NUMERIC		NOT NULL,	  
	  data_comentario	TIMESTAMP	NOT NULL,
	  PRIMARY KEY (id_comentario),
	  CONSTRAINT FK_Comentario_id_usuario
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario),
	  CONSTRAINT FK_Comentario_id_postagem
	    FOREIGN KEY (id_postagem)
	      REFERENCES Postagem(id_postagem)
);

CREATE SEQUENCE SEQ_USUARIO
INCREMENT 1
START 1;


CREATE SEQUENCE SEQ_SEGUIDOR
INCREMENT 1
START 1;

CREATE SEQUENCE SEQ_ENDERECO
INCREMENT 1
START 1;

CREATE SEQUENCE SEQ_CONTATO
INCREMENT 1
START 1;

CREATE SEQUENCE SEQ_POSTAGEM
INCREMENT 1
START 1;

CREATE SEQUENCE SEQ_COMENTARIO
INCREMENT 1
START 1;