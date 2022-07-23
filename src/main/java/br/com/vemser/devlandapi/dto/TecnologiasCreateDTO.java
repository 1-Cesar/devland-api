package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.entity.UsuarioEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnologiasCreateDTO {

    @Schema(description = "nome do desenvolvedor ou empresa", example = "João")
    @NotEmpty
    @Size(min = 1, max = 100)
    private String nomeTecnologia;

    private Integer idPessoa;

    //private UsuarioEntity usuario;

}
