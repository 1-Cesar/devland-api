package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Postagem {

    private Integer idPostagem;
    private Integer idUsuario;
    private TipoPostagem tipoPostagem;
    private String titulo;
    private String descricao;
    private String foto;
    private Integer curtidas;
    private LocalDateTime data;
}
