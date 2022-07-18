package br.com.vemser.devlandapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ComentarioCreateDTO {

    @Schema(description = "Id do Usuário que está comentando")
    @NotNull
    private Integer idUsuario;

    @Schema(description = "Descrição do comentário")
    @NotBlank
    private String descricao;

}
