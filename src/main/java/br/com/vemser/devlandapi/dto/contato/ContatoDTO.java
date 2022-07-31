package br.com.vemser.devlandapi.dto.contato;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContatoDTO extends ContatoCreateDTO {

    @Schema(description = "Id do contato do desenvolvedor ou empresa", example = "1")
    private Integer idContato;
}
