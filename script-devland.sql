CREATE TABLE Usuario (
	  id_usuario 		NUMBER 			NOT NULL,
	  nome 				VARCHAR2(100) 	NOT NULL,
	  email 			VARCHAR2(100) 	NOT NULL,	  	  
	  area_atuacao 		VARCHAR2(100) 	NOT NULL,
	  cpf_cnpj			VARCHAR2(14) 	NOT NULL UNIQUE,
	  tipo 				VARCHAR2(15)	NOT NULL,	  
	  PRIMARY KEY (id_usuario)	  
);

CREATE TABLE Seguidor (
	  id 				NUMBER 			NOT NULL,
	  id_seguidor 		NUMBER 			NOT NULL,
	  nome_seguidor		VARCHAR2(100) 	NOT NULL,
	  id_usuario 		NUMBER 			NOT NULL,	  
	  PRIMARY KEY (id),
	  CONSTRAINT FK_Seguidor_id_seguidor
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario)
);

CREATE TABLE Endereco (
	  id_endereco 		NUMBER 			NOT NULL,
	  id_usuario 		NUMBER 			NOT NULL,
	  tipo 				VARCHAR2(50) 	NOT NULL,
	  logradouro 		VARCHAR2(100) 	NOT NULL,
	  numero 			VARCHAR2(7) 	NOT NULL,
	  complemento 		VARCHAR2(20) 	NOT NULL,
	  cep 				CHAR(9) 		NOT NULL,
	  cidade 			VARCHAR2(100) 	NOT NULL,
	  estado 			VARCHAR2(50) 	NOT NULL,
	  pais 				VARCHAR2(50) 	NOT NULL,
	  PRIMARY KEY (id_endereco),
	  CONSTRAINT FK_Contato_id_endereco
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario)
);

CREATE TABLE Contato (
	  id_contato 		NUMBER 			NOT NULL,
	  id_usuario 		NUMBER 			NOT NULL,
	  tipo 				VARCHAR2(50) 	NOT NULL,
	  numero 			VARCHAR2(50) 	NOT NULL,	
	  descricao 		VARCHAR2(50)	NOT NULL,
	  PRIMARY KEY (id_contato),
	  CONSTRAINT FK_Contato_id_contato
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario)
);

CREATE TABLE Postagem (
	  id_postagem 		NUMBER 			NOT NULL,
	  id_usuario 		NUMBER 			NOT NULL,
	  tipo 				VARCHAR2(50) 	NOT NULL,
	  titulo 			VARCHAR2(50) 	NOT NULL,
	  descricao			VARCHAR2(1000) 	NOT NULL,	
	  ups				NUMBER			NOT NULL,
	  downs				NUMBER			NOT NULL,
	  views				NUMBER			NOT NULL,
	  data_postagem		DATE			NOT NULL,
	  PRIMARY KEY (id_postagem),
	  CONSTRAINT FK_Postagem_id_usuario
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario)
);

CREATE TABLE Comentario (
	  id_comentario		NUMBER			NOT NULL,	  
	  id_usuario 		NUMBER 			NOT NULL,	  
	  id_postagem 		NUMBER 			NOT NULL,
	  descricao			VARCHAR2(1000) 	NOT NULL,	
	  ups				NUMBER			NOT NULL,
	  downs				NUMBER			NOT NULL,	  
	  data_comentario	DATE			NOT NULL,
	  PRIMARY KEY (id_comentario),
	  CONSTRAINT FK_Comentario_id_usuario
	    FOREIGN KEY (id_usuario)
	      REFERENCES Usuario(id_usuario),
	  CONSTRAINT FK_Comentario_id_postagem
	    FOREIGN KEY (id_postagem)
	      REFERENCES Postagem(id_postagem)
);

CREATE SEQUENCE SEQ_USUARIO
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_SEGUIDOR
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_ENDERECO
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_CONTATO
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_POSTAGEM
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_COMENTARIO
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;