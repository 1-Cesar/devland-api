package br.com.vemser.devlandapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comentario {

    private Integer idComentario;
    private Integer idPostagem;
    private Integer idUsuario;
    private String descricao;
    private Integer curtidas;
    private String data;
    private Usuario usuario;

}
