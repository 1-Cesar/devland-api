package br.com.vemser.devlandapi.dto.endereco;

import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnderecoDTO extends EnderecoCreateDTO {

    @Schema(description = "id do endereco do desenvolvedor ou empresa", example = "1")
    private Integer idEndereco;

    private List<UsuarioDTO> usuarios;
}
