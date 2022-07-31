package br.com.vemser.devlandapi.dto.comentario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ComentarioCreateDTO {

    @Schema(description = "Descrição do comentário")
    @NotBlank
    private String descricaoComentarios;


}
