package br.com.vemser.devlandapi.dto.tecnologias;

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

    @Schema(description = "nome da tecnologia que o usuário conhece, uma por registro", example = "Spring Boot")
    @NotEmpty
    @Size(min = 1, max = 100)
    private String nomeTecnologia;

    //private Integer idTecnologia;

    //private UsuarioEntity usuario;

}
