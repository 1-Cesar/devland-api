package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.entity.UsuarioEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ComentarioCreateDTO {

    @Schema(description = "Descrição do comentário")
    @NotBlank
    private String descricaoComentarios;




}
