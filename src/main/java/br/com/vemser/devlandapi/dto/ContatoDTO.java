package br.com.vemser.devlandapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ContatoDTO extends ContatoCreateDTO {

    @Schema(description = "Id do contato do desenvolvedor ou empresa", example = "1")
    private Integer idContato;
}
