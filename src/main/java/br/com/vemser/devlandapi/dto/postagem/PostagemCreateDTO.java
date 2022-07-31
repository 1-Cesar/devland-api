package br.com.vemser.devlandapi.dto.postagem;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostagemCreateDTO {

    @Schema(description = "Tipo da PostagemEntity", example = "VAGAS, PROGRAMAS, PENSAMENTOS")
    @NotNull
    private TipoPostagem tipoPostagem;

    @Schema(description = "Título da PostagemEntity")
    @NotBlank
    private String titulo;

    @Schema(description = "Descrição da PostagemEntity")
    @NotBlank
    private String descricao;

    @Schema(description = "Descrição da PostagemEntity")
    private String foto;

    @Schema(description = "Id do Usuário")
    private Integer idUsuario;
}
