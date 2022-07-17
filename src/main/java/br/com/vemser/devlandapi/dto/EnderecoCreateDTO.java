package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.enums.TipoClassificacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoCreateDTO {

    @Schema(description = "id do desenvolvedor ou empresa", example = "1")
    private Integer idUsuario;

    @Schema(description = "RESIDENCIAL OU COMERCIAL", example = "COMERCIAL")
    @NotNull(message = "informe o tipo do endereço (RESIDENCIAL ou COMERCIAL)")
    private TipoClassificacao tipo;

    @Schema(description = "Rua.....ou Avenida....", example = "Rua do Java")
    @NotEmpty
    @Size(min = 6,max = 100)
    private String logradouro;

    @Schema(description = "numero do endereco", example = "123")
    @NotEmpty
    @Size(min = 1,max = 7)
    private String numero;

    @Schema(description = "Casa, Apto, etc.", example = "Apto")
    @Size(min = 4,max = 20)
    private String complemento;

    @Schema(description = "CEP com 8 digitos sem pontos ou traços", example = "12345678")
    @NotEmpty
    @Size(min = 8,max = 8)
    private String cep;

    @Schema(description = "cidade do endereco", example = "Cidade do Java")
    @NotEmpty
    @Size(min = 3,max = 100)
    private String cidade;

    @Schema(description = "SP, RS, etc.", example = "SP")
    @NotEmpty
    @Size(min = 2,max = 50)
    private String estado;

    @Schema(description = "País do endereco", example = "Javanês")
    @NotEmpty
    @Size(min = 3,max = 50)
    private String pais;
}
