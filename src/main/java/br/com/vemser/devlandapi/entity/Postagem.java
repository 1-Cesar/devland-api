package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Postagem {

    private Integer idPostagem;
    private Integer idUsuario;
    private TipoPostagem tipoPostagem;
    private String titulo;
    private String descricao;
    private Integer ups;
    private Integer downs;
    private Integer views;
    private Date data;
}
