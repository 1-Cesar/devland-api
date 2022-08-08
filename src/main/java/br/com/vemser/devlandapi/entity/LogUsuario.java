package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import org.springframework.data.annotation.Id;

@Document(collection = "log_usuario")
@Getter
@Setter
public class LogUsuario {

    @Id
    private Integer idUsuario;

    private String nome;

    private String areaAtuacao;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Enumerated(EnumType.STRING)
    private Genero genero;
}
