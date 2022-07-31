package br.com.vemser.devlandapi.dto.contato;

import br.com.vemser.devlandapi.enums.TipoClassificacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ContatoCreateDTO {

    @Schema(description = "Id do desenvolvedor ou empresa", example = "1")
    private Integer idUsuario;

    @Schema(description = "Número do contato", example = "11111111")
    @NotEmpty(message = "Número do contato precisa ser preenchido.")
    @Size(min = 8, max = 13, message = "Número inválido")
    private String numero;

    @Schema(description = "Whatsapp, Telegram, etc.", example = "Whatsapp")
    @NotEmpty(message = "Descrição deve ser preencida (ex.: whatsapp, telegram, observações, etc.)")
    private String descricao;

    @Schema(description = "RESIDENCIAL OU COMERCIAL", example = "RESIDENCIAL")
    @NotNull(message = "Informe o tipo do contato (RESIDENCIAL ou COMERCIAL)")
    private TipoClassificacao tipo;
}