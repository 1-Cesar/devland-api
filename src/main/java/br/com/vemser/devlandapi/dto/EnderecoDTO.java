package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.entity.UsuarioEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO extends EnderecoCreateDTO {

    @Schema(description = "id do endereco do desenvolvedor ou empresa", example = "1")
    private Integer idEndereco;
}
