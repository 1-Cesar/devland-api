package br.com.vemser.devlandapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO extends UsuarioCreateDTO {

    @Schema(description = "id do desenvolvedor ou empresa", example = "1")
    private Integer idUsuario;
}
