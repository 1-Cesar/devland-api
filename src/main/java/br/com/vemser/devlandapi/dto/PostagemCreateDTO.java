package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostagemCreateDTO {

    @Schema(description = "Tipo da Postagem")
    @NotNull
    private TipoPostagem tipoPostagem;

    @Schema(description = "Título da Postagem")
    @NotBlank
    private String titulo;

    @Schema(description = "Descrição da Postagem")
    @NotBlank
    private String descricao;

    @Schema(description = "Descrição da Postagem")
    private String foto;
}
