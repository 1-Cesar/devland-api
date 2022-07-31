package br.com.vemser.devlandapi.dto.postagem;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostagemDTO {

    @Schema(description = "Id da PostagemEntity")
    private Integer idPostagem;

    @Schema(description = "Tipo da PostagemEntity")
    private TipoPostagem tipoPostagem;

    @Schema(description = "Título da PostagemEntity")
    private String titulo;

    @Schema(description = "Descrição da PostagemEntity")
    private String descricao;

    @Schema(description = "Foto da PostagemEntity")
    private String foto;

    @Schema(description = "Número de Curtidas")
    private Integer curtidas;

    @Schema(description = "Data da PostagemEntity")
    private String data;


}
