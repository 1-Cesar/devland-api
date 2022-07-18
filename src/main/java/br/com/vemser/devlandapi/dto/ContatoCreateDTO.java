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
@AllArgsConstructor
@NoArgsConstructor
public class ContatoCreateDTO {

    @Schema(description = "id do desenvolvedor ou empresa", example = "1")
    private Integer idUsuario;

    @Schema(description = "numero do contato", example = "11111111")
    @NotEmpty
    @Size(min = 8, max = 13, message = "número inválido")
    private String numero;

    @Schema(description = "whatsapp, telegram, etc.", example = "Whatsapp")
    @NotEmpty(message = "Descrição deve ser preencida (ex: whatsapp, telegram, observações, etc.)")
    private String descricao;

    @Schema(description = "RESIDENCIAL OU COMERCIAL", example = "RESIDENCIAL")
    @NotNull(message = "informe o tipo do contato (RESIDENCIAL ou COMERCIAL)")
    private TipoClassificacao tipo;
}