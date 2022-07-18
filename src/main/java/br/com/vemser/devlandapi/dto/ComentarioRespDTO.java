package br.com.vemser.devlandapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ComentarioRespDTO {

    @Schema(description = "Id do Comentário")
    private Integer idComentario;

    @Schema(description = "Descrição do comentário")
    private String descricao;

    @Schema(description = "Número de Curtidas do Comentario")
    private Integer curtidas;

    @Schema(description = "Data do comentario")
    private String data;

    @Schema(description = "Usuário que comentou a postagem")
    private UsuarioDTO usuario;

}
