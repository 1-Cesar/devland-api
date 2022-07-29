package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ComentarioDTO extends ComentarioCreateDTO {
    //alterar para ComentarioDTO

    @Schema(description = "Id do Comentário")
    private Integer idComentario;

    @Schema(description = "Id do Usuário que está comentando")
    @NotNull
    private Integer idUsuario;

    @Schema(description = "Id da Postagem")
    @NotNull
    private Integer idPostagem;

    @Schema(description = "Número de Curtidas do ComentarioEntity")
    private Integer curtidasComentario;

    @Schema(description = "Data do comentario")
    private LocalDateTime dataComentario;

    @Schema(description = "Usuário que comentou a postagem")
    private UsuarioEntity usuario;

    private PostagemEntity postagem;

}
