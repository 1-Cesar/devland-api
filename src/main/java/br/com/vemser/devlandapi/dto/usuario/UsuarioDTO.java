package br.com.vemser.devlandapi.dto.usuario;

import br.com.vemser.devlandapi.dto.endereco.EnderecoDTO;
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
public class UsuarioDTO extends UsuarioCreateDTO {

    @Schema(description = "id do desenvolvedor ou empresa", example = "1")
    private Integer idUsuario;

    private List<EnderecoDTO> enderecoDTOS;

}
