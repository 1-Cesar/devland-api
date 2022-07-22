package br.com.vemser.devlandapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class ComentarioDTO extends ComentarioCreateDTO{
        //alterar para ComentarioDTO

    @Schema(description = "Id do Comentário")
    private Integer idComentario;

    @Schema(description = "Número de Curtidas do ComentarioEntity")
    private Integer curtidas;

    @Schema(description = "Data do comentario")
    private String data;

    @Schema(description = "Usuário que comentou a postagem")
    private UsuarioDTO usuario;

}
