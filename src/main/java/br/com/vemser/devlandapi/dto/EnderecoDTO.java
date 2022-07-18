package br.com.vemser.devlandapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO extends EnderecoCreateDTO {

    @Schema(description = "id do endereco do desenvolvedor ou empresa", example = "1")
    private Integer idEndereco;
}
